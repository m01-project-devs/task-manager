package com.m01project.taskmanager;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.file.Paths;

@Slf4j
@SpringBootApplication
public class TaskManagerApplication {

	public static void main(String[] args) {
		// Load .env file from project root for local development
		loadEnvironmentVariables();
		SpringApplication.run(TaskManagerApplication.class, args);
	}

	private static void loadEnvironmentVariables() {
		try {
			// Determine project root - check multiple possible locations
			String projectRoot = findProjectRoot();
			log.info("Loading .env from: {}", projectRoot);

			Dotenv dotenv = Dotenv.configure()
					.directory(projectRoot)
					.ignoreIfMissing()
					.load();

			// First pass: Load all variables
			dotenv.entries().forEach(entry -> {
				System.setProperty(entry.getKey(), entry.getValue());
			});

			// Second pass: Resolve variable interpolations (e.g., ${DB_HOST})
			dotenv.entries().forEach(entry -> {
				String resolvedValue = resolveVariables(entry.getValue());
				System.setProperty(entry.getKey(), resolvedValue);
				log.info("Loaded env: {} = {}", entry.getKey(), resolvedValue);
			});

			log.info(".env file loaded successfully!");
		} catch (Exception e) {
			// Silently continue - environment variables might be set by Docker or system
			log.info("Note: .env file not loaded (may be running in Docker): {}", e.getMessage());
		}
	}

	private static String findProjectRoot() {
		String currentDir = Paths.get("").toAbsolutePath().toString();

		// If we're in the backend folder, go up one level
		if (currentDir.endsWith("backend")) {
			return Paths.get("").toAbsolutePath().getParent().toString();
		}

		// Otherwise, check if .env exists in current directory
		if (Paths.get(currentDir, ".env").toFile().exists()) {
			return currentDir;
		}

		// Try parent directory
		String parentDir = Paths.get("").toAbsolutePath().getParent().toString();
		if (Paths.get(parentDir, ".env").toFile().exists()) {
			return parentDir;
		}

		// Default to current directory
		return currentDir;
	}

	private static String resolveVariables(String value) {
		if (value == null || !value.contains("${")) {
			return value;
		}

		String resolved = value;
		int maxIterations = 10; // Prevent infinite loops
		int iteration = 0;

		while (resolved.contains("${") && iteration < maxIterations) {
			String temp = resolved;
			int startIdx = resolved.indexOf("${");
			while (startIdx != -1) {
				int endIdx = resolved.indexOf("}", startIdx);
				if (endIdx != -1) {
					String varName = resolved.substring(startIdx + 2, endIdx);
					String varValue = System.getProperty(varName);

					if (varValue == null) {
						varValue = System.getenv(varName);
					}

					if (varValue != null) {
						resolved = resolved.substring(0, startIdx) + varValue + resolved.substring(endIdx + 1);
						break; // Restart the search after replacement
					}
				}
				startIdx = resolved.indexOf("${", startIdx + 1);
			}

			// If no change was made, break to avoid infinite loop
			if (temp.equals(resolved)) {
				break;
			}
			iteration++;
		}

		return resolved;
	}

}
