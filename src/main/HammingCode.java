package main;

import java.util.Scanner;
import java.util.ArrayList;


public class HammingCode {
	
	public static void main(String[] args) {
		
		displayInfo();
		
		try (Scanner scan = new Scanner(System.in)){
			
			int i = scan.nextInt();
			
			scan.nextLine();
			
			switch(i) {
				case (1): 
					manualBinaryValidate(scan);
					break;
				case (2):
					validateTest();
					break;
				default:
					System.out.println("Selection not recognized");
			}
			scan.close();
		}
		catch(Exception e){
			System.out.println("Invalid value! " + e);
		}	
		
		System.out.print("The application is finished...");
	}
	
	
	private static void displayInfo() {
		
		System.out.println("Hamming code");
		
		System.out.println("1 - Enter a binary message for verification");
		
		System.out.println("2 - Run tests");
	}
	
	
	private static void manualBinaryValidate(Scanner scan) {
		
		System.out.println("Enter correct binary message: ");
		
		String correctMessage = scan.nextLine();
		
		System.out.println("Enter test binary message: ");
		
		String testMessage = scan.nextLine();
		
		validateMessages(correctMessage, testMessage);
	}
	
	
	private static void validateTest() {
		
		String cor = "0100010000111101";
		String incor = "0110010000111101";
		
		cor = "100111100110";
		incor = "100111101110";
		
		validateMessages(cor, incor);
	}
	
	
	private static void validateMessages(String correctMessage, String testMessage) {
		
		ArrayList<Character> correctBits = calculateControlBits(correctMessage);
		ArrayList<Character> testBits = calculateControlBits(testMessage);
		
		ArrayList<Character> correctControlBits = new ArrayList<Character>();
		ArrayList<Character> testControlBits = new ArrayList<Character>();
		
		System.out.println("Bits of a Valid message:");
		displayBinaryBits(correctBits);
		System.out.println();
		System.out.println("Bits of a Test message:");
		displayBinaryBits(testBits);
		System.out.println();
		
		int indErrorBit = -1;
		
		for(int i = 0; i <= 4; i++) {
			int indControlBit = (int)Math.pow(2, i) - 1;
			
			correctControlBits.add(correctBits.get(indControlBit));
			testControlBits.add(testBits.get(indControlBit));
					
			if(correctBits.get(indControlBit) != testBits.get(indControlBit)) {
				indErrorBit = indControlBit;	
			}
		}
		
		System.out.println("Сontrol bits for a valid message:");
		displayBinaryBits(correctControlBits);
		System.out.println();
		System.out.println("Test message control bits:");
		displayBinaryBits(testControlBits);
		System.out.println();
		
		if(indErrorBit > -1) {
			System.out.println("Number of the control bit that did not match: " + indErrorBit);
			System.out.println("In valid message bit №" + indErrorBit + " = " + correctBits.get(indErrorBit));
			System.out.println("In test message bit №" + indErrorBit + " = " + testBits.get(indErrorBit));
		}
	}
	
	
	private static void displayBinaryBits(ArrayList<Character> message) {
		
		for(int i = 0; i < message.size(); i++) {
			System.out.print(message.get(i));
		}	
	}
	
	
	private static ArrayList<Character> calculateControlBits(String binaryMessage) {
		
		ArrayList<Character> hexadecimalMessage = createHexadecimalMessage(binaryMessage);
		
		int i = 0;
		int bit;
		
		do{
			bit = (int)Math.pow(2, i);
			
			hexadecimalMessage.set(bit - 1, checkParityBit(hexadecimalMessage, bit));
			
			i++;
			
		}while(bit != 16);	
		
		return hexadecimalMessage;
	}
	
	
	private static char checkParityBit(ArrayList<Character> hexadecimalMessage, int bit) {
		
		int count1 = 0;
		
		int i = bit - 1;
		
		while(i < hexadecimalMessage.size()) {
			for(int j = i; j < i + bit; j++) {
				if(j >= hexadecimalMessage.size()) {
					break;
				}
				if(hexadecimalMessage.get(j) == '1') {
					count1++;
				}
			}
			
			i += bit + bit;
		}
		
		if(count1 % 2 == 0) {
			return '0';
		}
		else {
			return '1';
		}
	}
	
	
	private static ArrayList<Character> addControlBits(ArrayList<Character> hexadecimalMessage){
		
		for(int i = 0; i <= 4; i++) { // 2^4 = 16 bit	
			hexadecimalMessage.add((int)Math.pow(2, i)-1, '0');
		}
		
		return hexadecimalMessage;		
	}
	
	
	private static ArrayList<Character> createHexadecimalMessage(String binaryMessage){
		
		ArrayList<Character> hexadecimalMessage = new ArrayList<Character>();
		
		for(char letter: binaryMessage.toCharArray()) {
			hexadecimalMessage.add(letter);
		}
		
		return addControlBits(hexadecimalMessage);
	}
	
	
}
