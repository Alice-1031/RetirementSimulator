package RetirementSimulator;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

/**
 * This class is a retirement simulator which takes user inputs specifying
 * expected investment,raise and return ranges to calculate probable yearly
 * investments,raises and returns and finally generates a simulation reflecting
 * the changes in the retirement account year by year.
 * 
 * <p>
 * Bugs: program crashes if user inputs decimal in yearsToRetirement
 * 
 * @author Shreya_Jaiswal
 */
public class RetirementSimulator {

    
	/* Scanner class variable that takes user input for the entire program */
	public static Scanner input;
	/* Random class variable stores random values generated between [0,1) */
	public static Random r;
	/* balance in retirement account at year 0 input by user */
	private static double initialInvest;
	/* salary at year 0 input by user */
	private static double initialSalary;
	/* range of amounts as percentage of salary planned to save in a given year */
	private static double investRangeMin;
	private static double investRangeMax;
	/*
	 * range of amounts as percentage of investment expected to earn or lose in a
	 * given year
	 */
	private static double returnRangeMin;
	private static double returnRangeMax;
	/*
	 * range of pay raise as percentage of salary expected to receive or lose in a
	 * given year
	 */
	private static double raiseRangeMin;
	private static double raiseRangeMax;
	/* number of yearsToRetire until retirement */
	private static int yearsToRetire;
	/* statistics to be printed out at the end */
	private static double minInvestment, maxInvestment, avgInvestment;
	private static double minRaise, maxRaise, avgRaise;
	private static double minReturn, maxReturn, avgReturn;
	private static double maxSalaryUsed;
	/* array to hold simulated yearly values in retirement account */
	private static double[] retirementAccount;
	/* array to hold increased salary every year beginning with initial */
	private static double[] currentSalary;
	/* arrays to hold generated random ranges */
	private static double[] ri;
	private static double[] rr;
	private static double[] rret;
	/* holds number of hash tags to be used with each value in histogram */
	private static int[] hash;
 
	/**
	 * prompts user to input value until positive value is entered
	 */
	private static void getInitialInvestment() {
		do {

			System.out.println("Please enter the current amount you have in your retirement account:  ");
			initialInvest = input.nextDouble();
			if (initialInvest < 0) {
				System.out.println(
						"ERROR: The retirement account balance must be zero or greater. Please re-enter your current amount you have in your retirement account: ");
			}
		} while (initialInvest < 0);
	}

	/**
	 * prompts user to input value until positive value is entered
	 */
	private static void getInitialSalary() {
		do {
			System.out.println("Please enter your current salary:");
			initialSalary = input.nextDouble();
			if (initialSalary < 0) {
				System.out.println(
						"ERROR: The retirement account balance must be zero or greater. Please re-enter your current amount you have in your retirement account:");
			}
		} while (initialSalary < 0);
	}

	/**
	 * prompts user to input 2 values until positive values are entered and 2nd
	 * value greater than 1st
	 */
	private static void getSalaryInvestmentRange() {
		do {
			System.out.println(
					"As a percentage of salary, please enter the minimum amount you plan to save for retirement in any given year:");
			investRangeMin = input.nextDouble();
			if (investRangeMin < 0) {
				System.out.println("ERROR: amount must be 0 or greater");
			}
		} while (investRangeMin < 0);
		do {
			System.out.println(
					"As a percentage of salary, please enter the maximum amount you plan to save for retirement in any given year:");
			investRangeMax = input.nextDouble();
			if (investRangeMax < investRangeMin) {
				System.out.println("ERROR: planned maximum saving must be greater than planned minimum saving");
			}
		} while (investRangeMax < investRangeMin);
	}

	/**
	 * prompts user to input 2 values until 2nd value is greater than 1st
	 */

	private static void getInvestmentReturnRange() {
		do {
			System.out
					.println("As a percentage, please enter the expected minimum yearly return for your investments:");
			returnRangeMin = input.nextDouble();
			System.out
					.println("As a percentage, please enter the expected maximum yearly return for your investments:");
			returnRangeMax = input.nextDouble();
			if (returnRangeMax < returnRangeMin) {
				System.out.println("ERROR:expected maximum return must be greater than expected minimum return");
			}
		} while (returnRangeMax < returnRangeMin);
	}

	/**
	 * prompts user to input 2 values 1st is greater than -100 and 2nd is greater
	 * than 1st
	 */
	private static void getYearlyRaiseRange() {
		do {
			System.out.println(
					"As a percentage,please enter the minimum pay raise you expect to receive in any given year:");
			raiseRangeMin = input.nextDouble();
			if (raiseRangeMin < -100) {
				System.out.println("ERROR: you cannot lose what you never had");
			}
		} while (raiseRangeMin < -100);

		do {
			System.out
					.println("As a percentage please, the maximum pay raise you expect to receive in any given year:");
			raiseRangeMax = input.nextDouble();
			if (raiseRangeMax < raiseRangeMin) {
				System.out.println("ERROR: expected maximum raise must be greater than expected minimum raise");
			}
		} while (raiseRangeMax < raiseRangeMin);

	}

	/**
	 * prompts user to input a value
	 */
	private static void getYearsToRetirement() {

		System.out.println("Plase enter the number of years until you retire: ");
		yearsToRetire = input.nextInt();

	} // program would crash if user entered decimals

	/**
	 * @return random range between variables investRangeMin and investRangeMax
	 */
	private static double randInvst() {

		return investRangeMin + (investRangeMax - investRangeMin) * r.nextDouble();

	}

	/**
	 * @return random range between variables raiseRangeMin and raseRangeMax
	 */

	private static double randRaise() {
		return raiseRangeMin + (raiseRangeMax - raiseRangeMin) * r.nextDouble();
	}

	/**
	 * @return random range between variables returnRangeMin and returnRangeMax
	 */

	private static double randReturn() {
		return returnRangeMin + (returnRangeMax - returnRangeMin) * r.nextDouble();
	}

	/**
	 * @param array with double type values
	 * @return minimum value stored in array
	 */
	private static double findMinimum(double[] array) {

		int element;
		double result;

		element = 0;

		for (int i = 0; i < array.length; i++) {
			if (array[i] < array[element]) {
				element = i;
			}
		}
		result = array[element];
		return result;
	}

	/**
	 * @param array with double type values
	 * @return maximum value stored in array
	 */
	private static double findMaximum(double[] array) {
		int element;
		double result;

		element = 0;

		for (int i = 0; i < array.length; i++) {
			if (array[i] > array[element]) {
				element = i;
			}
		}

		result = array[element];

		return result;
	}

	/**
	 * gathers and verifies userInputs
	 */
	public static void userInput() {
		input = new Scanner(System.in);
		getInitialInvestment();
		getInitialSalary();
		getSalaryInvestmentRange();
		getInvestmentReturnRange();
		getYearlyRaiseRange();
		getYearsToRetirement();
		input.close();

	}

	/**
	 * stores year by year simulated values in the array retirementAccount
	 */
	private static void runSimulation() {
		r = new Random();

		avgInvestment = 0;
		avgRaise = 0;
		avgReturn = 0;

		currentSalary = new double[yearsToRetire + 1];
		retirementAccount = new double[yearsToRetire + 1];

		ri = new double[yearsToRetire + 1];
		rr = new double[yearsToRetire + 1];
		rret = new double[yearsToRetire + 1];

		minInvestment = ri[0];
		minRaise = rr[0];
		minReturn = rret[0];
		maxInvestment = ri[0];
		maxRaise = rr[0];
		maxReturn = rret[0];
		maxSalaryUsed = currentSalary[0];

		retirementAccount[0] = initialInvest;
		currentSalary[0] = initialSalary;

		// calculating balance in retirement account every year beginning year 1
		for (int i = 1; i < retirementAccount.length; i++) {

			double temp; // temporarily holds values to be manipulated

			ri[i] = randInvst(); // random ranges
			rr[i] = randRaise();
			rret[i] = randReturn();
			
			

			temp = retirementAccount[i - 1] + (ri[i] / 100) * currentSalary[i - 1];
			temp = (1 + rret[i] / 100) * temp;
			retirementAccount[i] = temp; // savings and returns added to previous year's retirement account balance and
											// stored in current

			// since the last increased salary value is never used in the calculation its
			// not added in the array to avoid error when finding maxSalaryUsed
			if (i < retirementAccount.length - 1) {
				currentSalary[i] = currentSalary[i - 1] * (1 + rr[i] / 100);
			}
			// salary from previous year used to calculate this year's balance,raised and
			// then stored in the current year to be used next year
			
			if (ri[i] < minInvestment) {
				minInvestment = ri[i];
			}
			
			if(rr[i]< minRaise) {
				minRaise = rr[i];
			}
			
			if(rret[i]<minReturn) {
				minReturn = rret[i];
			}
			
			if(ri[i]>maxInvestment) {
				maxInvestment = ri[i];
			}
			if(rr[i]>maxRaise) {
				maxRaise = rr[i];
			}
			if(rret[i]>maxReturn) {
				maxReturn = rret[i];
			}
			if(currentSalary[i] > maxSalaryUsed) {
				maxSalaryUsed = currentSalary[i];
			}

			avgInvestment = avgInvestment + ri[i];
			avgRaise = avgRaise + rr[i];
			avgReturn = avgReturn + rret[i]; // summing up the ranges to be averaged

		} // for

		/**ri[0] = ri[1];
		rr[0] = rr[1];
		rret[0] = rret[1]; // since 0th element of range arrays is never used, equate them to any other
							// value in array
							// to avoid error while finding min and max range used

		minInvestment = findMinimum(ri);
		minRaise = findMinimum(rr);
		minReturn = findMinimum(rret);
		maxInvestment = findMaximum(ri);
		maxRaise = findMaximum(rr);
		maxReturn = findMaximum(rret);
		maxSalaryUsed = findMaximum(currentSalary);**/

		avgInvestment = avgInvestment / yearsToRetire;
		avgRaise = avgRaise / yearsToRetire;
		avgReturn = avgReturn / yearsToRetire;

	}// runSimulation

	/*
	 * @param takes number of tags to be generated
	 * 
	 * @return returns a string with n number of hash tags
	 */
	private static String hashGenerator(int n) {
		String s;

		s = "#";

		for (int i = 0; i < n; i++) {
			s = s.concat("#");
		}

		return s;
	}

	/*
	 * prints user inputs,final results and generated histogram
	 */

	public static void printResults() {

		hash = new int[retirementAccount.length];
		DecimalFormat df = new DecimalFormat("#.##");

		System.out.println("\nYou Entered:");
		System.out.println("Initial Investment: $" + initialInvest);
		System.out.println("Initial Salary: $" + initialSalary);
		System.out.println("Yearly percentage of salary saved: " + investRangeMin + "% - " + investRangeMax + "%");
		System.out.println("Range of yearly returns: " + returnRangeMin + "% - " + returnRangeMax + "%");
		System.out.println("Yearly salary increase range: " + raiseRangeMin + "% - " + raiseRangeMax + "%");
		System.out.println("Number of years until retirement: " + yearsToRetire + "\n");
		System.out.println("The simulation generated the following values: ");

		System.out.printf("Yearly percentage of salary saved:-- min: %.2f%% max: %.2f%% average: %.2f%%\n",
				minInvestment, maxInvestment, avgInvestment);
		System.out.printf("Range of yearly returns:-- min: %.2f%% max: %.2f%% average: %.2f%%\n", minReturn, maxReturn,
				avgReturn);
		System.out.printf("Yearly percentage of salary increase:-- min: %.2f%% max: %.2f%% average: %.2f%%\n", minRaise,
				maxRaise, avgRaise);
		System.out.printf("The maximum salary used in the simulation was: $%.2f", maxSalaryUsed);
		System.out.printf("\n\nThe following histogram shows the yearly activity for your investment account: \n");
		System.out.printf("\n\nYear:\n");

		

		for (int i = 0; i < retirementAccount.length; i++) {

			double temp = (100 * retirementAccount[i]) / findMaximum(retirementAccount);
			int integer = (int) temp;
			hash[i] = integer;

		} // stores number of tags to be assigned to each value in retirementAccount array
			// in hash array

		for (int i = 0; i < retirementAccount.length; i++) {

			System.out.println(i + ":" + hashGenerator(hash[i]) + df.format(retirementAccount[i]));

		} // for

	}

	/*
	 * main method drives the entire simulator
	 */
	public static void main(String[] args) {

		userInput();
		runSimulation();
		printResults();

	}// main

}// class
