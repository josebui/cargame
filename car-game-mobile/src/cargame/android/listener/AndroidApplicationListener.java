package cargame.android.listener;

import com.badlogic.gdx.LifecycleListener;

/**
 * The listener interface for receiving androidApplication events.
 * The class that is interested in processing a androidApplication
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addAndroidApplicationListener<code> method. When
 * the androidApplication event occurs, that object's appropriate
 * method is invoked.
 *
 * @see AndroidApplicationEvent
 */
public class AndroidApplicationListener implements LifecycleListener {


	/* (non-Javadoc)
	 * @see com.badlogic.gdx.LifecycleListener#pause()
	 */
	@Override
	public void pause() {
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.LifecycleListener#resume()
	 */
	@Override
	public void resume() {}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.LifecycleListener#dispose()
	 */
	@Override
	public void dispose() {
	}
	
}
