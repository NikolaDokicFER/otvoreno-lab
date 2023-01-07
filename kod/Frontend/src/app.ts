import express from 'express';
import fs from 'fs';
import path from 'path'
import https from 'https';
import dotenv from 'dotenv'
import bodyParser from 'body-parser'
import {Pool} from 'pg'
import { auth, requiresAuth, claimEquals} from 'express-openid-connect'; 
import { parse } from 'json2csv';

dotenv.config()
const app = express();

app.set("views", path.join(__dirname, "views"));
app.use("/styles",express.static(__dirname + "/styles"));
app.use("/opendata",express.static(__dirname + "/opendata"));
app.use("/scripts",express.static(__dirname + "/scripts"));
app.set('view engine', 'ejs');

var jsonParser = bodyParser.json()

var urlencodedParser = bodyParser.urlencoded({extended: false})


app.set("views", path.join(__dirname, "views"));
app.use("/styles",express.static(__dirname + "/styles"));
app.set('view engine', 'ejs');

const externalUrl = process.env.RENDER_EXTERNAL_URL;
const port = externalUrl && process.env.PORT ? parseInt(process.env.PORT) : 4080

const config = {
  authRequired: false,
  auth0Logout: true,
  baseURL: externalUrl ||  `https://localhost:${port}`,
  clientID: process.env.CLIENT_ID,
  issuerBaseURL: process.env.ISSUER_BASE_URL,
  secret: process.env.SECRET
};

const pool = new Pool()
app.use(auth(config));
  
app.get('/',(req, res) => {
  if(req.oidc.isAuthenticated()){
    res.redirect('/index')
  }else{
    res.redirect('/public')
  }
});

app.get('/public', (req, res) => {
  if(req.oidc.isAuthenticated()){
    res.render('index', {
      user: req.oidc.idTokenClaims
  });}else{
    res.render('public',  {
    });
  }
});

app.get('/index',(req, res) => {
  if(req.oidc.isAuthenticated()){
    res.render('index', {
      user: req.oidc.idTokenClaims
  });}else{
    res.render('error',  {
    });
  }
});

app.get('/profile', (req, res) => {
  if(req.oidc.isAuthenticated()){
    res.render('profile', {
      user: req.oidc.idTokenClaims
  });}else{
    res.render('error',  {
    });
  }
})

app.get('/refresh', jsonParser ,async (req, res) => {
  if(req.oidc.isAuthenticated()){
    const resp = await pool.query(`SELECT * FROM players NATURAL JOIN tournament`)
    var players = resp.rows

    fs.writeFileSync('src\\opendata\\atp_players.json', JSON.stringify(players));

    const csv = parse(players)

    fs.writeFileSync('src\\opendata\\atp_players.csv', csv);
    res.redirect('/index')
    }else{
      res.render('error',  {
      });
    }
})

app.get('/datatable', urlencodedParser, async (req, res) =>{
  if(req.oidc.isAuthenticated()){
    var playersQueried
    try{
      const resp = await pool.query(`SELECT * FROM players NATURAL JOIN tournament`)
      playersQueried = resp.rows
    }catch(err){
      playersQueried = []
    }
   
    res.render('datatable', {
      players: playersQueried
    });
  }else{
    res.render('error',  {
    });
  }
});

app.post('/datatable', jsonParser, async (req, res) => {
  if(req.oidc.isAuthenticated()){
    var playersQueried;
    let attr = req.body.attr
    let query = req.body.query

    try{
      if(attr == "all"){
        const resp = await pool.query(`SELECT * FROM players NATURAL JOIN tournament WHERE to_tsvector(players::text) @@ to_tsquery(\'${query}\') OR to_tsvector(tournament::text) @@ to_tsquery(\'${query}\')`)
        playersQueried = resp.rows
      }else{
        const resp = await pool.query(`SELECT * FROM players NATURAL JOIN tournament WHERE ${attr}=$1`, [query])
        playersQueried = resp.rows
      }
    }catch(err){
      playersQueried = []
    }
    
    fs.writeFileSync('src\\opendata\\filter_players.json', JSON.stringify(playersQueried));
    

    let table = '<table>\
                        <tr>\
                        <th width="20px">Atp</th>\
                        <th width="80px">First Name</th>\
                        <th width="120px">Last Name</th>\
                        <th width="100px">Country</th>\
                        <th width="50px">Height(cm)</th>\
                        <th width="50px">Weight(kg)</th>\
                        <th width="80px">Strong hand</th>\
                        <th width="100px">Racket</th>\
                        <th width="150px">Coach</th>\
                        <th width="100px">Tournament</th>\
                        <th width="50px">Tier</th>\
                        <th width="50px">Result</th>\
                        </tr>'

    for (var player of playersQueried){ 
          table = table + '<tr>\
                        <td>'+ player.atprank +'</td>\
                        <td>'+ player.firstname +'</td>\
                        <td>'+ player.lastname +'</td>\
                        <td>'+ player.country +'</td>\
                        <td>'+ player.height +'</td>\
                        <td>'+ player.weight +'</td>\
                        <td>'+ player.stronghand  +'</td>\
                        <td>'+ player.racket  +'</td>\
                        <td>'+ player.coach  +'</td>\
                        <td>'+ player.city +'</td>\
                        <td>'+ player.tier +'</td>\
                        <td>'+ player.result +'</td>\
                      </tr>'
    }

    table = table +  '</table>'

    res.send({html: table});
  }else{
    res.render('error',  {
    });
  }
});

if (externalUrl) {
  const hostname = '127.0.0.1';
  app.listen(port, hostname, () => {
  console.log(`Server locally running at http://${hostname}:${port}/ and from
  outside on ${externalUrl}`);
  });
}else{
  https.createServer({
    key: fs.readFileSync('server.key'),
    cert: fs.readFileSync('server.cert')
    }, app)
    .listen(port, function () {
      console.log(`Server running at https://localhost:${port}/`);
      });
}