package com.mybetfair.model;

public class Market {
	private String name;
	private double id;
	private int exchangeId;
	private String marketType;
	private String marketStartTime;
	private int numberOfWinners;

	public Market() {
	}

	public Market(double id, String name, int exchangeId, String marketType, String marketStartTime,
			int numberOfWinners) {
		super();
		this.name = name;
		this.id = id;
		this.exchangeId = exchangeId;
		this.marketType = marketType;
		this.marketStartTime = marketStartTime;
		this.numberOfWinners = numberOfWinners;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getId() {
		return id;
	}

	public void setId(double id) {
		this.id = id;
	}

	public int getExchangeId() {
		return exchangeId;
	}

	public void setExchangeId(int exchangeId) {
		this.exchangeId = exchangeId;
	}

	public String getMarketType() {
		return marketType;
	}

	public void setMarketType(String marketType) {
		this.marketType = marketType;
	}

	public String getMarketStartTime() {
		return marketStartTime;
	}

	public void setMarketStartTime(String marketStartTime) {
		this.marketStartTime = marketStartTime;
	}

	public int getNumberOfWinners() {
		return numberOfWinners;
	}

	public void setNumberOfWinners(int numberOfWinners) {
		this.numberOfWinners = numberOfWinners;
	}

}
