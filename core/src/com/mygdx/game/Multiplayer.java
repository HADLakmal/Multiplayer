package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class Multiplayer extends ApplicationAdapter implements KeyListener {

	private final float UPDATE_TIME = 1/60f;
	private float timer;
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

	public void updateServer(float dt){

		timer+=dt;
		if (timer>=UPDATE_TIME && car!=null && car.hasMoved()){
			JSONObject object = new JSONObject();
			try {
				object.put("x",car.getX());
				object.put("y",car.getY());
				socket.emit("playerMoved",object);
			}catch (JSONException e){

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
		updateServer(Gdx.graphics.getDeltaTime());
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
					Gdx.app.log("SocketIO", "Error");
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
		}).on("playerDisconnected", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONObject jdata = (JSONObject) args[0];
				try {
					String id = jdata.getString("id");
					map.remove(id);
				} catch (JSONException e) {
					Gdx.app.log("SocketIO","Error");
				}
			}
		}).on("playerMoved", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONObject jdata = (JSONObject) args[0];
				try {
					String id = jdata.getString("id");
					Double x = jdata.getDouble("x");
					Double y = jdata.getDouble("y");
					if (map.get(id)!=null){
						map.get(id).setPosition(x.floatValue(),y.floatValue());
					}

				} catch (JSONException e) {
					Gdx.app.log("SocketIO","Error");
				}
			}
		}).on("getPlayer", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONArray jarray = (JSONArray) args[0];
				try {
					for (int r=0; r<jarray.length();r++){
						Car cocar = new Car(enemyPlayer);
						Vector2 position = new Vector2();
						position.x = ((Double)jarray.getJSONObject(r).getDouble("x")).floatValue();
						position.y = ((Double)jarray.getJSONObject(r).getDouble("y")).floatValue();
						cocar.setPosition(position.x,position.y);
						map.put(jarray.getJSONObject(r).getString("id"), cocar);
					}
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
