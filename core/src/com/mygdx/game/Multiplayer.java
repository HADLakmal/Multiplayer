package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import org.json.JSONException;
import org.json.JSONObject;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class Multiplayer extends ApplicationAdapter implements KeyListener {
	SpriteBatch batch;
	Texture img;
	private Socket socket;
	private Car car;

	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		connect();
		identfySocketEvent();
		car = new Car(null,0,0);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
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
					Gdx.app.log("SocketIO", "My ID :" + id);
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
