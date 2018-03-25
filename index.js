'use strict'

const express = require('express')
const bodyParser = require('body-parser')
const request = require('request')
const app = express()
//new stuff
//

app.use(bodyParser.json()); 

app.set('port', (process.env.PORT || 5000))

// Process application/x-www-form-urlencoded
app.use(bodyParser.urlencoded({extended: false}))

// Process application/json
app.use(bodyParser.json())

// Index route
app.get('/', function (req, res) {
	res.send('Hello, this is generic response!')
})

//catch java app 
app.post('/random', function (req, res) {
	const result = rateProf(req.body.text, function(text) {

	res.send(text)
})
	

})
//

// for Facebook verification
app.get('/webhook/', function (req, res) {
	if (req.query['hub.verify_token'] === 'kirilmudev') { 
		res.send(req.query['hub.challenge'])
	}
	res.send('Error, wrong token')
})



// Spin up the serverres
app.listen(app.get('port'), function() {
	console.log('running on port', app.get('port'))
})

//new stuff
/*
app.post('/webhook/', function (req, res) {
    let messaging_events = req.body.entry[0].messaging
    for (let i = 0; i < messaging_events.length; i++) {
	    let event = req.body.entry[0].messaging[i]
	    let sender = event.sender.id
	    if (event.message && event.message.text) {
		    let text = event.message.text
		    //sendTextMessage(sender, "Text received, echo: " + text.substring(0, 200))
		rateProf(text)
		continue
	    }
    }
    res.sendStatus(200)
}) */

var sender
app.post('/webhook/', function (req, res) {
    let messaging_events = req.body.entry[0].messaging
    for (let i = 0; i < messaging_events.length; i++) {
        let event = req.body.entry[0].messaging[i]
        if(event){
            if(event.message){
                sender = event.sender.id
                if(event.message.text)
                {
                    let text = event.message.text
                    console.log(text);
                    rateProf(text, function() {});
                    continue;
                }
            }
        }
    }
res.sendStatus(200)
})

const token = "EAAcwnHSzA1cBANiIwWMU455IrYVg2HDnfBc15aAzZBM0hzpzFftZANwvV7dBsWheQZAIuwUjwRWX7XJwEzLYbfI3JxKDa03J7MDkZBVIAi8liqzfxKBXlXMYzyJtuRA7ZAfyXZCGeYI1ZAPuWhlpKqQF8Ni9MSItPdoy7MXkNHunAZDZD"

function sendTextMessage(sender, text) {
    let messageData = { text:text }
    request({
	    url: 'https://graph.facebook.com/v2.6/me/messages',
	    qs: {access_token:token},
	    method: 'POST',
		json: {
		    recipient: {id:sender},
			message: messageData,
		}
	}, function(error, response, body) {
		if (error) {
		    console.log('Error sending messages: ', error)
		} else if (response.body.error) {
		    console.log('Error: ', response.body.error)
	    }
    })
}

  var Client = require('node-wolfram');
  var Wolfram = new Client('KAUW78-88P5E7APTE');  

  function rateProf(text, cb) {

    var callback = function(e,result){
    if (!result) {
        console.log("return");
        return;
    }
        console.log("in");
        var flag = 0
        console.log("inside"); 
        if(result)
        {
            console.log("Result emp:")
            if(result.queryresult)
            {
                console.log("QueryResult emp:")
                if(result.queryresult.pod)
                {
                    console.log("Pod emp:")
                    for(var a=1; a<(result.queryresult.pod.length>3?3:result.queryresult.pod.length); a++)
                    {
                        var pod = result.queryresult.pod[a];
                        if(!pod)
                        {
                            console.log("return");
                        }
                        else
                        {
                            console.log(pod.$.title,": ");
                            for(var b=0; b<(pod.subpod.length>3?3:pod.subpod.length); b++)
                            {
                                var subpod = pod.subpod[b];
                                if(subpod)
                                {
                                    console.log("Subpod")
                                    for(var c=0; c<subpod.plaintext.length; c++)
                                    {
                                        var text = subpod.plaintext[c];
                                        console.log('\t', text);
                                        sendTextMessage(sender,text)
					cb(text) //newly added
                                        flag = 1

                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if(flag == 0)
        sendTextMessage(sender,"Not ready for this question, but soon!")
    }


    Wolfram.query(text,callback);
};
	


