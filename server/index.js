var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);
var players = [];

//listen on the port 8080
server.listen(8080, function(){
	console.log("Server is now running...");
});

//listning for incoming conncetion
io.on('connection', function(socket){
	console.log("Player Connected!");
	socket.emit('socketID',{id: socket.id});
	socket.broadcast.emit('newPlayer',{id: socket.id});
	players.push(new Player(socket.id,0,0));
	socket.on('disconnect', function(){
		console.log("Player Disconnected");
		
	});
});

function player(id,x,y){
	this.id = id;
	this.x = x;
	this.y =y;
}
