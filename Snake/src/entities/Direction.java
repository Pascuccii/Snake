package entities;

public enum Direction {

	LEFT("LEFT",'L'),
	RIGHT("RIGHT",'R'),
	UP("UP",'U'),
	DOWN("DOWN",'D');
	
	public final String name;
	public final char directionCode;
	
	Direction(String name, char directionCode) {
		this.name = name;
		this.directionCode = directionCode;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
