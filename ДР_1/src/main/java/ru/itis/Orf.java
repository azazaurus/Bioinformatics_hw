package ru.itis;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class Orf {
	ArrayList<Triplet> arrayList = new ArrayList<>();
	int length;
	String protein;
	int startIndex;
	int finishIndex;
	Chain chain;
	int frame;
	boolean isStoped = false;
	boolean isStarted = false;

	public enum Chain {FORWARD, REVERSE}

	public void startOrf(Triplet startTriplet, int startIndex, Chain chain, int frame) {
		arrayList.add(startTriplet);
		this.startIndex = startIndex;
		this.chain = chain;
		this.frame = frame;
		isStarted = true;
	}

	public void addTriplet(Triplet triplet) {
		arrayList.add(triplet);
	}

	public void stopOrf(Triplet stopTriplet, int finishIndex) {
		arrayList.add(stopTriplet);
		this.finishIndex = finishIndex;
		this.length = arrayList.size() - 1;
		isStoped = true;
	}

	public String getOrf() {
		return arrayList.stream()
			.collect(Collector.of(
				StringBuilder::new,
				(stringBuilder, triplet) -> stringBuilder.append(triplet.triplet).append(' '),
				StringBuilder::append))
			.toString();
	}

	public String getTranslatedProtein() {
		if (!isStoped)
			throw new IllegalStateException("ORF not yet completed");
		StringBuilder translatedProtein = new StringBuilder();
		for (Triplet triplet : arrayList) {
			translatedProtein.append(triplet.getTranslatedProtein()).append(" ");
		}
		return translatedProtein.toString();
	}

	public int getLength() {
		return length;
	}

	public boolean isStoped() {
		return isStoped;
	}

	public boolean isStarted() {
		return isStarted;
	}
}