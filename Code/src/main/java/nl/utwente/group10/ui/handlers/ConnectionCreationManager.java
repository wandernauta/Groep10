package nl.utwente.group10.ui.handlers;

import nl.utwente.group10.ui.CustomUIPane;
import nl.utwente.group10.ui.components.Connection;
import nl.utwente.group10.ui.components.ConnectionAnchor;
import nl.utwente.group10.ui.components.InputAnchor;
import nl.utwente.group10.ui.components.OutputAnchor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ConnectionCreationManager {

	CustomUIPane pane;

	/**
	 * Touch points have an ID associated with each specific touch point, this
	 * is the ID associated with the Mouse.
	 */
	public static final Integer MOUSE_ID = 0;
	/**
	 * Maps an (Touch or Mouse) ID to a line, used to keep track of what touch
	 * point is dragging what line.
	 */
	private Map<Integer, Connection> connections;

	public ConnectionCreationManager(CustomUIPane pane) {
		this.pane = pane;
		connections = new HashMap<Integer, Connection>();
	}

	public Connection createConnectionWith(int id, ConnectionAnchor anchor) {
		Connection newConnection = null;
		if (anchor instanceof OutputAnchor) {
			newConnection = new Connection((OutputAnchor) anchor);
		} else if (anchor instanceof InputAnchor) {
			newConnection = new Connection((InputAnchor) anchor);
		}
		pane.getChildren().add(newConnection);
		anchor.startFullDrag();
		connections.put(id, newConnection);
		return newConnection;
	}
	
	public Connection createTouchConnectionWith(int id, ConnectionAnchor anchor) {
		Connection newConnection = null;
		if (anchor instanceof OutputAnchor) {
			newConnection = new Connection((OutputAnchor) anchor);
		} else if (anchor instanceof InputAnchor) {
			newConnection = new Connection((InputAnchor) anchor);
		}
		pane.getChildren().add(newConnection);
		connections.put(id, newConnection);
		return newConnection;
	}

	public Connection finishConnection(int id, ConnectionAnchor anchor) {
		Connection connection = connections.get(id);
		if (connection != null) {
			if (anchor.canConnect() && connection.addAnchor(anchor)) {
				pane.invalidate();
			} else {
				finishConnection(id);
			}
		}
		System.out.println(connection);
		connections.remove(id);
		return connection;
	}

	public Connection finishConnection(int id) {
		Connection connection = connections.get(id);
		connections.put(id, null);
		if (connection != null) {
			connection.disconnect();
			pane.getChildren().remove(connection);
			connections.remove(id);
		}
		return null;
	}

	public void editConnection(int id, ConnectionAnchor anchor) {
		Optional<ConnectionAnchor> anchorToKeep = anchor.getOtherAnchor();
		if (anchor.isConnected() && anchorToKeep.isPresent()) {
			Connection connection = anchor.getConnection().get();
			connection.disconnect(anchor);
			anchorToKeep.get().startFullDrag();
			connections.put(id, connection);
			pane.invalidate();
		}
	}
	
	public void editConnectionTouch(int id, ConnectionAnchor anchor) {
		Optional<ConnectionAnchor> anchorToKeep = anchor.getOtherAnchor();
		System.out.println("Editing1: "+anchor.getConnection());
		if (anchor.isConnected() && anchorToKeep.isPresent()) {
			Connection connection = anchor.getConnection().get();
			System.out.println("Editing2: "+connection);
			connection.disconnect(anchor);
			connections.put(id, connection);
			pane.invalidate();
		}
	}

	public void updateLine(int id, double x, double y) {
		if (connections.get(id) != null) {
			connections.get(id).setFreeEnds(x, y);
		}
	}
}
