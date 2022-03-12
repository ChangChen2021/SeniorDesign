package testOptimized;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import main.fields.VectorField;

public class Equal {
	private final static double maxResolution = 0.000000000001;
	private final static double samplingSistnace = 0.5;
	private final static Vector2D xRange = new Vector2D(-10,10);
	private final static Vector2D yRange = new Vector2D(-10,10);
	
	public static boolean equal(VectorField[] fields) {
		for (VectorField field : fields) {
			for(double x = xRange.getX(); x < xRange.getY(); x += samplingSistnace) {
				for(double y = yRange.getX(); y < yRange.getY(); y += samplingSistnace) {
					double distance = field.getFunctionValue(x, y)
							.distance(field.getFunctionValueOptimized(x, y));
					if (distance > maxResolution) {
						
						//Check potential Border Conflict
						double distanceN = field.getFunctionValue(x, y)
								.distance(field.getFunctionValueOptimized(x + maxResolution, y));
						double distanceE = field.getFunctionValue(x, y)
								.distance(field.getFunctionValueOptimized(x, y + maxResolution));
						double distanceS = field.getFunctionValue(x, y)
								.distance(field.getFunctionValueOptimized(x - maxResolution, y));
						double distanceW = field.getFunctionValue(x, y)
								.distance(field.getFunctionValueOptimized(x, y - maxResolution));
						//diagonal
						double distanceNE = field.getFunctionValue(x, y)
								.distance(field.getFunctionValueOptimized(x + maxResolution, y + maxResolution));
						double distanceSE = field.getFunctionValue(x, y)
								.distance(field.getFunctionValueOptimized(x - maxResolution, y + maxResolution));
						double distanceSW = field.getFunctionValue(x, y)
								.distance(field.getFunctionValueOptimized(x - maxResolution, y - maxResolution));
						double distanceNW = field.getFunctionValue(x, y)
								.distance(field.getFunctionValueOptimized(x + maxResolution, y - maxResolution));
						
						
						if (distanceN < maxResolution || distanceE < maxResolution 
								|| distanceS < maxResolution || distanceW < maxResolution
								|| distanceNE < maxResolution || distanceSE < maxResolution
								|| distanceSW < maxResolution || distanceNW < maxResolution) {
							//Border conflict resolved
						} else {
							System.out.print(x + " " + y);
							System.out.print(field.getFunctionValue(x, y));
							System.out.println(field.getFunctionValueOptimized(x, y));
							double time = System.currentTimeMillis();
							field.export(new Vector2D(-5,5), samplingSistnace, new Vector2D(-5,5), samplingSistnace, Double.toString(time));
							field.exportUsingOpt(new Vector2D(-5,5), samplingSistnace, new Vector2D(-5,5), samplingSistnace, Double.toString(time) + "opt");
							return false;
						}
					}
				}
			}
		}	
		return true;
	}
	
	
	
	public static boolean equalFields(VectorField field1, VectorField field2) {
		
		for(double x = xRange.getX(); x < xRange.getY(); x += samplingSistnace) {
			for(double y = yRange.getX(); y < yRange.getY(); y += samplingSistnace) {
				double distance = field1.getFunctionValueOptimized(x, y)
						.distance(field2.getFunctionValueOptimized(x, y));
				if (distance > maxResolution) {
					
					//Check potential Border Conflict
					Vector2D f1Point = field1.getFunctionValueOptimized(x, y);
					double distanceN = f1Point
							.distance(field2.getFunctionValueOptimized(x + maxResolution, y));
					double distanceE = f1Point
							.distance(field2.getFunctionValueOptimized(x, y + maxResolution));
					double distanceS = f1Point
							.distance(field2.getFunctionValueOptimized(x - maxResolution, y));
					double distanceW = f1Point
							.distance(field2.getFunctionValueOptimized(x, y - maxResolution));
					//diagonal
					double distanceNE = f1Point
							.distance(field2.getFunctionValueOptimized(x + maxResolution, y + maxResolution));
					double distanceSE = f1Point
							.distance(field2.getFunctionValueOptimized(x - maxResolution, y + maxResolution));
					double distanceSW = f1Point
							.distance(field2.getFunctionValueOptimized(x - maxResolution, y - maxResolution));
					double distanceNW = f1Point
							.distance(field2.getFunctionValueOptimized(x + maxResolution, y - maxResolution));
					
					
					if (distanceN < maxResolution || distanceE < maxResolution 
							|| distanceS < maxResolution || distanceW < maxResolution
							|| distanceNE < maxResolution || distanceSE < maxResolution
							|| distanceSW < maxResolution || distanceNW < maxResolution) {
						//Border conflict resolved
					} else {
						System.err.println(x + " " + y);
						System.err.println(field1.getFunctionValueOptimized(x, y));
						System.err.println(field2.getFunctionValueOptimized(x, y));
						return false;
					}
				}
			}
		}
	
		return true;
	}
}
