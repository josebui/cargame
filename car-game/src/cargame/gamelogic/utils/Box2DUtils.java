package cargame.gamelogic.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;

public class Box2DUtils {

	public static Body createPolygonBody(World world, Vector2 pos, float width, float height,float density, float friction, float restitution,boolean dynamic,boolean sensor){
        BodyDef bodyDef = new BodyDef();  
        if(dynamic){
        	bodyDef.type = BodyType.DynamicBody;
        }
        bodyDef.position.set(pos.cpy());  
        Body body = world.createBody(bodyDef);  
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(width,height);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;  
        fixtureDef.density = density;  
        fixtureDef.friction = friction;  
        fixtureDef.restitution = restitution;
        fixtureDef.isSensor = sensor;
        body.createFixture(fixtureDef);
        body.setBullet(dynamic);
        body.resetMassData();
        return body;
	}
	
	public static Vector2 rotateVector(Vector2 vec, double angle){
		return new Vector2((float)(Math.sin(angle) * vec.x),(float) (-Math.cos(angle) * vec.y));
	}
	
}
