package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import org.json.JSONException;
import org.json.JSONObject;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class Multiplayer extends ApplicationAdapter implements KeyListener {
	SpriteBatch batch;
	Texture img;
	private Socket socket;
	private Car car;
	Texture player;
	Texture enemyPlayer;
	HashMap<String,Car> map;

	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("stone.jpg");
		player = new Texture("Tank1.png");
		enemyPlayer = new Texture("Tank2.png");
		map = new HashMap<String, Car>();


		connect();
		identfySocketEvent();
	}

	public void handleInput(float t){

		if (car!=null){
			if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){
				car.setPosition(car.getX()+(-200*t),car.getY());
			}else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
				car.setPosition(car.getX()+(200*t),car.getY());
			}
		}
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, 0, 0);
		handleInput(Gdx.graphics.getDeltaTime());
		if (car!=null) car.draw(batch);
		for (HashMap.Entry<String,Car> entry:map.entrySet()){
			entry.getValue().draw(batch);
		}
		batch.end();
	}
	
	@Override
	public void dispose () {
		player.dispose();
		enemyPlayer.dispose();
	}
	public void connect(){
		try {

			socket = IO.socket("http://localhost:8080");
			socket.connect();
		}catch (Exception e){
			System.out.println(e);
		}
	}

	public void identfySocketEvent() {
		//configure connection to the serve
		socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				//log into the console
				Gdx.app.log("SocketIO", "Connected");
				car = new Car(player);

			}
		}).on("socketID", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONObject jdata = (JSONObject) args[0];
				try {
					String id = jdata.getString("id");
					Gdx.app.log("SocketIO", "My ID :" + id);
				} catch (JSONException e) {
					Gdx.app.log("SocketIO","Error");
				}
			}
		}).on("newPlayer", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONObject jdata = (JSONObject) args[0];
				try {
					String id = jdata.getString("id");
					Gdx.app.log("SocketIO", "new player ID :" + id);
					map.put(id, new Car(enemyPlayer));
				} catch (JSONException e) {
					Gdx.app.log("SocketIO","Error");
				}
			}
		});
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {


	}

	@Override
	public void keyReleased(KeyEvent e) {

	}
}
