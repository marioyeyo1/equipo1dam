package com.campusfp.hitogrupal.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Menu {
	// ATTRIBUTES
	private Map<String, List<String>> menuMap;

	// CONSTRUCTOR
	public Menu() {
		this.menuMap = new HashMap<>();
		menuMap.put("main", Arrays.asList("Notificación de tiempo", "Notificación custom", "Enviar archivo"));
	}

    // METHDOS
    private void printHeader() {
        System.out.println("o==============================o");
        System.out.println("o=            MENU            =o");
        System.out.println("o==============================o");
    }

    private void printSeparator() {
        System.out.println("o==============================o");
    }
    
    private void printEmptyLine() {
        System.out.println("o                              o");
    }

	private String embedOption(int num, String option) {
		String out = "o  ";
		out += num + ". " + option;
		while (out.length() < 30) {
			out += " ";
		}
		out += " o";
		return out;
	}

	public void updateFileList(List<String> fileList) {
		menuMap.put("files", fileList);
	}

	public void printMenu(String category) {
		if (!menuMap.containsKey(category)) {
			System.out.println("ERROR DE MENU: No existe la categoria " + category);
			return;
		}
		List<String> options = menuMap.get(category);

		printHeader();
		for (int i = 0; i < options.size(); i++) {
			System.out.println(embedOption(i + 1, options.get(i)));
		}
		printEmptyLine();
		System.out.println(embedOption(0, "Salir"));
		printSeparator();
	}

	public static void confirmMsg(String msg, Scanner sc) {
		System.out.println(msg);
		System.out.println("Presiona INTRO para continuar...");
		sc.nextLine();
	}

	public static void clearTerminal() {
		System.out.print("\033[H\033[2J");
    	System.out.flush();
	}

    // GETTERS AND SETTERS
    
    
}
