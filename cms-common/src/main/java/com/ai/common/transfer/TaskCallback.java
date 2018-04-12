package com.ai.common.transfer;

public class TaskCallback {
	public boolean begining(long taskId, long totalSize, long beginSize) {
		return true;
	}

	public boolean percent(long taskId, double percent) {
		return true;
	}

	public boolean success(long taskId) {
		return true;
	}

	public boolean fail(long taskId, int times, String message) {
		return true;
	}
}
