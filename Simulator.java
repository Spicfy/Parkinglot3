/**
 * @author Mehrdad Sabetzadeh, University of Ottawa
 *
 */
public class Simulator {

	/**
	 * Length of car plate numbers
	 */
	public static final int PLATE_NUM_LENGTH = 3;

	/**
	 * Number of seconds in one hour
	 */
	public static final int NUM_SECONDS_IN_1H = 3600;

	/**
	 * Maximum duration a car can be parked in the lot
	 */
	public static final int MAX_PARKING_DURATION = 8 * NUM_SECONDS_IN_1H;

	/**
	 * Total duration of the simulation in (simulated) seconds
	 */
	public static final int SIMULATION_DURATION = 24 * NUM_SECONDS_IN_1H;

	/**
	 * The probability distribution for a car leaving the lot based on the duration
	 * that the car has been parked in the lot
	 */
	public static final TriangularDistribution departurePDF = new TriangularDistribution(0, MAX_PARKING_DURATION / 2,
			MAX_PARKING_DURATION);

	/**
	 * The probability that a car would arrive at any given (simulated) second
	 */
	private Rational probabilityOfArrivalPerSec;

	/**
	 * The simulation clock. Initially the clock should be set to zero; the clock
	 * should then be incremented by one unit after each (simulated) second
	 */
	private int clock;

	/**
	 * Total number of steps (simulated seconds) that the simulation should run for.
	 * This value is fixed at the start of the simulation. The simulation loop
	 * should be executed for as long as clock < steps. When clock == steps, the
	 * simulation is finished.
	 */
	private int steps;

	/**
	 * Instance of the parking lot being simulated.
	 */
	private ParkingLot lot;

	/**
	 * Queue for the cars wanting to enter the parking lot
	 */
	private Queue<Spot> incomingQueue;

	/**
	 * Queue for the cars wanting to leave the parking lot
	 */
	private Queue<Spot> outgoingQueue;

	/**
	 * @param lot   is the parking lot to be simulated
	 * @param steps is the total number of steps for simulation
	 */
	public Simulator(ParkingLot lot, int perHourArrivalRate, int steps) {
	
		//throw new UnsupportedOperationException("This method has not been implemented yet!");
		this.lot = lot;

		this.clock = 0;

		this.steps = steps;

		this.probabilityOfArrivalPerSec = new Rational(perHourArrivalRate, 3600);

		incomingQueue = new LinkedQueue<Spot>();
		outgoingQueue = new LinkedQueue<Spot>();

	}


	/**
	 * Simulate the parking lot for the number of steps specified by the steps
	 * instance variable
	 * NOTE: Make sure your implementation of simulate() uses peek() from the Queue interface.
	 */
	public void simulate() {
	
		//throw new UnsupportedOperationException("This method has not been implemented yet!");
		while (clock<steps){
			if (CarArrivesThisSecond(probabilityOfArrivalPerSec)){
				String string = RandomGenerator.generateRandomString(PLATE_NUM_LENGTH);
				Car car = new Car(string);
				Spot spot = new Spot(car, clock);
				incomingQueue.enqueue(spot);

			}
			for (int i = 0;i<lot.getOccupancy();i++){
				Spot parkedcar = lot.getSpotAt(i);
				if (parkedcar==null){

				}
				else{
					if (clock-parkedcar.getTimestamp()==MAX_PARKING_DURATION){
						outgoingQueue.enqueue(parkedcar);
						lot.remove(i);

					}
					else if (clock-parkedcar.getTimestamp()<MAX_PARKING_DURATION){
						if (RandomGenerator.eventOccurred(departurePDF.pdf(parkedcar.getTimestamp()))){
							outgoingQueue.enqueue(parkedcar);
							lot.remove(i);
						}
					}
				}
			}
			if (!incomingQueue.isEmpty()){
				Spot s = incomingQueue.peek();
				Car c = s.getCar();
				if (lot.attemptParking(c, clock)){
					lot.park(c, clock);
				//	System.out.println(c+" ENTERED at timestep "+clock+"; occupancy is at "+lot.getOccupancy());
					incomingQueue.dequeue(); 
				}

			}
			if (!outgoingQueue.isEmpty()){
				Spot s = outgoingQueue.dequeue();
				Car c = s.getCar();
				//System.out.println(c+" EXITED at timestep "+clock+"; occupancy is at "+lot.getOccupancy());
				

			}

			clock ++;
		}

	}

	private boolean CarArrivesThisSecond(Rational probability){
		boolean probabilistically = RandomGenerator.eventOccurred(probability);
		return probabilistically; 
		
	}

	public int getIncomingQueueSize() {
	
		//throw new UnsupportedOperationException("This method has not been implemented yet!");
		return incomingQueue.size();
	}
}
