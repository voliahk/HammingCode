package main;

import java.util.Scanner;
import java.util.ArrayList;

public class HammingCode {
	
	static ArrayList<Integer> NUM_OF_CONTROL_BITS = new ArrayList<>();
	
	
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
		
		System.out.print("\nThe application is finished...");
	}
	
	
	private static void displayInfo() {
		
		System.out.println("Код хэмминга");
		
		System.out.println("1 - Ввести значения вручную");
		
		System.out.println("2 - Запустить тесты");
	}
	
	
	private static void manualBinaryValidate(Scanner scan) {
		
		System.out.println("Введите правильное сообщение: ");
		
		String correctMessage = scan.nextLine();
		
		System.out.println("Введите неправильное сообщение: ");
		
		String testMessage = scan.nextLine();
		
		validateMessages(correctMessage, testMessage);
	}
	
	
	private static void validateTest() {
		// TODO add tests
		String cor =   "1100010000110101";
		String incor = "1100010000110100";
		
		//cor =   "0110100001100001";
		//incor = "0110100001100001";
		
		//cor =   "10";
		//incor = "11";
		
		
		validateMessages(cor, incor);
	}
	
	
	private static void validateMessages(String correctMessage, String testMessage) {
		setControlBits(correctMessage.length());
		ArrayList<Character> correctBits = calculateParityBits(correctMessage);
		ArrayList<Character> testBits = calculateParityBits(testMessage);
		
		System.out.println("Правильное сообщение с контрольными битами: \n" 
							+ getStringRepresentation(correctBits));
		System.out.println("Неправильное сообщение с контрольными битами: \n" 
							+ getStringRepresentation(testBits));
		
		int errorBit = getErrorBit(correctBits, testBits);
		
		if(errorBit > -1) {
			System.out.println("\nНАЙДЕНА ОШИБКА!!!");
			System.out.println("\nНомер ошибочного бита: " + errorBit);
			
			System.out.println("В правильном сообщении это бит №" + errorBit 
								+ " = " + correctBits.get(errorBit));
			
			System.out.println("В неправильном сообщении это бит №" + errorBit 
								+ " = " + testBits.get(errorBit));
			
			System.out.println("\nБиты исправленного сообщения: \n" + сorrectTheError(testBits, errorBit));
			
		}
		else {
			System.out.println("\nОшибки нет. Биты совпадают.");
		}
		System.out.println("Биты правильного сообщения: \n" + correctMessage);
		System.out.println("Биты неправильного сообщения: \n" + testMessage);
	}
	
	private static String getStringRepresentation(ArrayList<Character> list) {
		
		StringBuilder builder = new StringBuilder(list.size());
		
		for(char bit: list) {
			builder.append(bit);
		}
		
		return builder.toString();
	}
	
	// Исправление ошибки
	private static String сorrectTheError(ArrayList<Character> incorrectMessage, int errorBit) {
		char result = (incorrectMessage.get(errorBit) % 2) == 0 ? '1': '0';
		incorrectMessage.set(errorBit, result);
		
		for(int controlBit = NUM_OF_CONTROL_BITS.size() - 1; controlBit >= 0; controlBit--) {
			incorrectMessage.remove((int)NUM_OF_CONTROL_BITS.get(controlBit));
		}
		
		return getStringRepresentation(incorrectMessage);		
	}
	
	// Поиск ошибочного контрольного бита
	private static int getErrorBit(ArrayList<Character> correctMessages, 
									ArrayList<Character> incorectMessages){
		
		int numOfIncorrectBit = 0;
		
		for(Integer controlBit: NUM_OF_CONTROL_BITS) {
			if(correctMessages.get(controlBit) != incorectMessages.get(controlBit)) {
				numOfIncorrectBit += controlBit + 1;
			}
		}	

		return numOfIncorrectBit - 1;
	}
	
	// Вычисление контрольных бит
	private static ArrayList<Character> calculateParityBits(String binaryMessage) {
		ArrayList<Character> messageWithControlBits = getControlBitMessage(binaryMessage);
		ArrayList<Character> encodedMessage = new ArrayList<>(messageWithControlBits);
		
		for(Integer controlBit: NUM_OF_CONTROL_BITS) {
			encodedMessage.set(controlBit, isEvenOrOdd(getTotalSumOfControlledBits(messageWithControlBits, controlBit)));
		}	
		
		return encodedMessage;
	}
	
	// Вычисление суммы контрольного бита
	private static int getTotalSumOfControlledBits(ArrayList<Character> encodedMessage, int controlBit) {
		int totalSumOfBits = 0;
		int stepBit = controlBit+1;
		int startBit = stepBit;
		int currentBit = startBit; 
		
		while(currentBit - 1 < encodedMessage.size()) {
			
			totalSumOfBits += Character.getNumericValue(encodedMessage.get(currentBit - 1));
			currentBit++;
			
			if(currentBit >= startBit + stepBit) {
				startBit += stepBit + stepBit;
				currentBit = startBit;
			}
			
		}
		return totalSumOfBits;
	}
	
	// Определение четности бита
	private static char isEvenOrOdd(int totalSumOfBits) {
		if(totalSumOfBits % 2 == 0) {
			return '0';
		}
		return '1';
	}
	
	// Добавление сообщению контрольных бит
	private static ArrayList<Character> getControlBitMessage(String binaryMessage){
		
		ArrayList<Character> encodedMessage = new ArrayList<Character>();
		
		// Перевод строки в ArrayList<Character>
		for(char letter: binaryMessage.toCharArray()) {
			encodedMessage.add(letter);
		}
		
		for(Integer controlBit: NUM_OF_CONTROL_BITS) {
			encodedMessage.add(controlBit, '0');
		}
		
		return encodedMessage;
	}	
	
	// Определение контрольных битов для сообщения n-размера
	private static void setControlBits(int messageSize) {
		int i = 0;
		int parityBit = 0;
		
		while(parityBit < messageSize + NUM_OF_CONTROL_BITS.size()) {
			NUM_OF_CONTROL_BITS.add(parityBit);
			i++;
			parityBit = ((int)Math.pow(2, i)) - 1;
		}
		System.out.println(NUM_OF_CONTROL_BITS.toString());
	}
	
}
