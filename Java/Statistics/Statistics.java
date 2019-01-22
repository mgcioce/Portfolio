/*
*	Statistics.java is a program that runs some statistical functions for the user,
* 	such as sample mean, sample variance, and sample standard deviation
*
*	date: 11/14/17
*	Author: Mike Cioce
*	Version: 0.0
*/

import java.util.*;
import java.io.*;
public class Statistics
{
	private final int INITIAL_SIZE = 16;

	Scanner inputFile;
	double [] sampleData;
	double sampleMean;
	double sampleVariance;
	double sampleSD;
	int arrayElements;
	public Statistics(Scanner input)
	{
		arrayElements = 0;
		inputFile = input;
		//read the input into the double array;
		populateSampleData();
		sampleMean = findMean();
		sampleVariance = findVariance();
		sampleSD = findSD();
		System.out.println(toString());
	}

	public String toString()
	{
		return "\nThe sample size is " + arrayElements + "\nThe sample mean is " + sampleMean + "\nThe sample variance is "
		+ sampleVariance + "\nThe sample standard deviation is " + sampleSD;
	}
	/**
	* findSD() calculates the sample standard deviation sigma and
	* returns it as a double;
	* @return double - returns the standard deviation as a double value;
	*/
	private double findSD()
	{
		return Math.pow(sampleVariance, 0.5);
	}

	/**
	* findVariance() calculates the sample variance sigma squared
	* and returns it as a double value;
	* @return double - returns a double value that is the sample variance
	* of the data set;
	*/
	private double findVariance()
	{
		double sum = 0;
		double xSubIMinusXBar = 0;
		for (int i = 0; i < arrayElements ; i++)
		{
			xSubIMinusXBar = sampleData[i] - sampleMean;
			xSubIMinusXBar = xSubIMinusXBar * xSubIMinusXBar;
			sum += xSubIMinusXBar;
		}
		return (sum / (arrayElements - 1));
	}
	/**
	* findMean() calculates the sample mean (x-bar) and returns it as a
	* double value;
	* @return double - returns a double value that is the value of x-bar
	*/
	private double findMean()
	{
		double sum = 0;
		for (int i = 0; i < arrayElements; i++)
		{
			sum += sampleData[i];
		}
		return ( sum / arrayElements);
	}
	/**
	*	populateSampleData() populates the data array with the data values from
	* the input text file;
	*/
	private void populateSampleData()
	{
		sampleData = new double[INITIAL_SIZE];
		inputFile.useDelimiter(new String(" "));
		while(inputFile.hasNext())
		{
			sampleData[arrayElements] = inputFile.nextDouble();
			arrayElements++;
			if (arrayElements >= ((3 * sampleData.length) / 4))
				sampleData = resizeArray();
		}
		inputFile.close();
	}
	private double [] resizeArray()
	{
		double [] newArray = new double[sampleData.length * 2];
		for(int i = 0; i < arrayElements; i++)
		{
			newArray[i] = sampleData[i];
		}
		return newArray;
	}
	public static void main(String [] args) throws FileNotFoundException
	{
		//1)get text file input from the user
		Scanner kb = new Scanner(System.in); //creates a Scanner object to capture
																				//keyboard input from the user;
		System.out.println("Please enter the filepath for the data you want to run "
			+ "statistical methods for \n(For example: C:\\Users\\Mike\\Programs\\data.txt)"); //user prompt for filepath
		String fileName = kb.nextLine(); // stores filepath literal in a String object
		Scanner file = new Scanner(new File(fileName)); // creates a scanner to get input from
																					//a file the user created
    //2) create a new Statistics object with the data file;
		Statistics stat = new Statistics(file);


	}
}
