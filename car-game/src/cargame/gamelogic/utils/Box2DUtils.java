package cargame.gamelogic.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;

/**
 * The Class Box2DUtils.
 */
public class Box2DUtils {

	/**
	 * Creates the polygon body.
	 *
	 * @param world the world
	 * @param pos the pos
	 * @param width the width
	 * @param height the height
	 * @param density the density
	 * @param friction the friction
	 * @param restitution the restitution
	 * @param dynamic the dynamic
	 * @param sensor the sensor
	 * @return the body
	 */
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
	
	/**
	 * Rotate vector.
	 *
	 * @param vec the vec
	 * @param angle the angle
	 * @return the vector2
	 */
	public static Vector2 rotateVector(Vector2 vec, double angle){
		return new Vector2((float)(Math.sin(angle) * vec.x),(float) (-Math.cos(angle) * vec.y));
	}
	
}
