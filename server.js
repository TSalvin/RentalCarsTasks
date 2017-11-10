var express = require('express');
var exec = require('child_process').exec;
var app = express();

app.get('/', function(req, res) {
	res.send('Simple REST API');
});
app.get('/tasks', function(req, res) {
	var child = exec('java -jar RentalCarsTasks.jar',
        function (error, stdout, stderr){
        res.send(stdout);
        if(error !== null){
        	res.send('An error occured, check the console for more details');
            console.log("Error -> "+error);
        }
    });
});

app.listen(3000);

console.log('Listening on port 3000');
