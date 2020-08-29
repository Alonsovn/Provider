package com.provider.app.model;

public class Provider {

	private int id, units, user;
	private String weight, volume, value, trasnportType, warehouse, deliveryDate;

	public Provider() {
	}


	public Provider(int id, int units, int user, String weight, String volume, String value, String trasnportType,
			String warehouse, String deliveryDate) {
		super();
		this.id = id;
		this.units = units;
		this.user = user;
		this.weight = weight;
		this.volume = volume;
		this.value = value;
		this.trasnportType = trasnportType;
		this.warehouse = warehouse;
		this.deliveryDate = deliveryDate;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getUnits() {
		return units;
	}


	public void setUnits(int units) {
		this.units = units;
	}


	public int getUser() {
		return user;
	}


	public void setUser(int user) {
		this.user = user;
	}


	public String getWeight() {
		return weight;
	}


	public void setWeight(String weight) {
		this.weight = weight;
	}


	public String getVolume() {
		return volume;
	}


	public void setVolume(String volume) {
		this.volume = volume;
	}


	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		this.value = value;
	}


	public String getTrasnportType() {
		return trasnportType;
	}


	public void setTrasnportType(String trasnportType) {
		this.trasnportType = trasnportType;
	}


	public String getWarehouse() {
		return warehouse;
	}


	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}


	public String getDeliveryDate() {
		return deliveryDate;
	}


	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}


	@Override
	public String toString() {
		return "Customer [id=" + id + ", weight=" + weight + ", volume=" + volume + ", units=" + units
				+ ", trasnsport-type=" + trasnportType + ", warehouse=" + warehouse + ", user=" + user
				+ ", delivery date=" + deliveryDate + "]";
	}

}