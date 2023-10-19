package main;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;


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
		// TODO add tests
		String cor = "0100010000111101";
		String incor = "0110010000111101";
		
		cor = "100111100110";
		incor = "100111101110";
		
		validateMessages(cor, incor);
	}
	
	
	private static void validateMessages(String correctMessage, String testMessage) {
		
		ArrayList<Character> correctBits = calculateControlBits(correctMessage);
		ArrayList<Character> testBits = calculateControlBits(testMessage);
		
		System.out.println("Bits of a Valid message: " + getStringRepresentation(correctBits));
		System.out.println("Bits of a Test message: " + getStringRepresentation(testBits));
		
		HashMap<String, String> results = checkForInconsistency(correctBits, testBits);
		
		System.out.println("Сontrol bits for a valid message: " + results.get("ValidBits"));
		System.out.println("Test message control bits: " + results.get("TestBits"));
		
		if(results.containsKey("ErrorBit")) {
			System.out.println("Number of the control bit that did not match: " + results.get("ErrorBit"));
			
			System.out.println("In valid message bit №" + results.get("ErrorBit") + " = "
					+ correctBits.get(Integer.parseInt(results.get("ErrorBit"))));
			
			System.out.println("In test message bit №" + results.get("ErrorBit") + " = "
					+ testBits.get(Integer.parseInt(results.get("ErrorBit"))));
		}
	}
	
	private static HashMap<String, String> checkForInconsistency(
			ArrayList<Character> correctBits, 
			ArrayList<Character> testBits){
		
		HashMap<String, String> results = new HashMap<String, String>();
		
		StringBuilder correctControlBits = new StringBuilder();
		StringBuilder testControlBits = new StringBuilder();
		
		int i = 0;
		int indControlBit = ((int)Math.pow(2, i)) - 1;
		
		while(indControlBit < correctBits.size()) {
			correctControlBits.append(correctBits.get(indControlBit));
			testControlBits.append(testBits.get(indControlBit));
			
			if(correctBits.get(indControlBit) != testBits.get(indControlBit)) {
				results.put("ErrorBit", Integer.toString(indControlBit));	
			}
			
			i++;
			indControlBit = ((int)Math.pow(2, i)) - 1;
		}
		
		results.put("ValidBits", correctControlBits.toString());
		results.put("TestBits", testControlBits.toString());
		
		return results;
	}
	
	
	private static String getStringRepresentation(ArrayList<Character> list) {
		
		StringBuilder builder = new StringBuilder(list.size());
		
		for(char bit: list) {
			builder.append(bit);
		}
		
		return builder.toString();
	}
	
	
	private static ArrayList<Character> calculateControlBits(String binaryMessage) {
		
		ArrayList<Character> hexadecimalMessage = createHexadecimalMessage(binaryMessage);
		
		int i = 0;
		int bit = ((int)Math.pow(2, i)) - 1;
		int messageSize = hexadecimalMessage.size();
		
		while(bit < messageSize){
			hexadecimalMessage.set(bit, checkParityBit(hexadecimalMessage, bit));
			
			i++;
			
			bit = ((int)Math.pow(2, i)) - 1;	
		};	
		
		return hexadecimalMessage;
	}
	
	
	private static char checkParityBit(ArrayList<Character> hexadecimalMessage, int bit) {
		// TODO refactor names
		int count1 = 0;
		bit += 1;
		int i = bit;
		
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
		
		int i = 0;
		int bit = ((int)Math.pow(2, i)) - 1;
		int messageSize = hexadecimalMessage.size();
		
		while(bit < messageSize) {
			hexadecimalMessage.add(bit, '0');
			i++;
			bit = ((int)Math.pow(2, i)) - 1;
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
