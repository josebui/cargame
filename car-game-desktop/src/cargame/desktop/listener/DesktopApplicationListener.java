package cargame.desktop.listener;

import cargame.desktop.gui.CarGameUI;

import com.badlogic.gdx.LifecycleListener;

/**
 * The listener interface for receiving desktopApplication events.
 * The class that is interested in processing a desktopApplication
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addDesktopApplicationListener<code> method. When
 * the desktopApplication event occurs, that object's appropriate
 * method is invoked.
 *
 * @see DesktopApplicationEvent
 */
public class DesktopApplicationListener implements LifecycleListener {

	/** The car game ui. */
	private CarGameUI carGameUI;
	
	/**
	 * Instantiates a new desktop application listener.
	 *
	 * @param carGameUI the car game ui
	 */
	public DesktopApplicationListener(CarGameUI carGameUI) {
		super();
		this.carGameUI = carGameUI;
	}

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
		if(carGameUI != null)
			carGameUI.toggleButton();
	}
	
}
