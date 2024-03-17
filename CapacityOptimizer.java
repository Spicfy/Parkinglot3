public class CapacityOptimizer {
	private static final int NUM_RUNS = 10;

	private static final double THRESHOLD = 5.0d;

	public static int getOptimalNumberOfSpots(int hourlyRate) {
		int n = 1;
		int sum = 0;
		System.out.println("==== Setting lot capacity to: " + n + "====");
		for(int i = 1; i <= NUM_RUNS; i++){
			long start = System.currentTimeMillis();
			ParkingLot lot = new ParkingLot(n);
			Simulator sim = new Simulator(lot, hourlyRate,86400);
			sim.simulate();
			int queueSize = sim.getIncomingQueueSize();
			long end = System.currentTimeMillis();

			System.out.println("Simulation run "+ n + "(" + (end - start)+"); Queue length at the end of simulation run: "+ queueSize);

			sum += queueSize;
		}
		double avg = sum/NUM_RUNS;
		if (avg <= THRESHOLD){
			return n;
		}
		else{
			n++;
			return getOptimalNumberOfSpots(hourlyRate);
		}

	
		
	
	}

	public static void main(String args[]) {
		int a = getOptimalNumberOfSpots(3);
		System.out.println(a);
	
		StudentInfo.display();

		long mainStart = System.currentTimeMillis();

		if (args.length < 1) {
			System.out.println("Usage: java CapacityOptimizer <hourly rate of arrival>");
			System.out.println("Example: java CapacityOptimizer 11");
			return;
		}

		if (!args[0].matches("\\d+")) {
			System.out.println("The hourly rate of arrival should be a positive integer!");
			return;
		}

		int hourlyRate = Integer.parseInt(args[0]);

		int lotSize = getOptimalNumberOfSpots(hourlyRate);

		System.out.println();
		System.out.println("SIMULATION IS COMPLETE!");
		System.out.println("The smallest number of parking spots required: " + lotSize);

		long mainEnd = System.currentTimeMillis();

		System.out.println("Total execution time: " + ((mainEnd - mainStart) / 1000f) + " seconds");

	}
}