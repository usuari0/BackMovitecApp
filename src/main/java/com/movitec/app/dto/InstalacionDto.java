package com.movitec.app.dto;

import java.util.Date;

import com.movitec.app.entity.Empleado;
import com.movitec.app.entity.Equipo;
import com.movitec.app.entity.Plan;

public class InstalacionDto {
	private Integer id;
	private Equipo equipo;
	private Date fecha;
	private String serie;
	private String direccion;
	private Empleado empleado;
	private Plan plan;
	
	public InstalacionDto() {}
	
	public InstalacionDto(Integer id, Equipo equipo, Date fecha, String serie,String direccion, Empleado empleado, Plan plan) {
		super();
		this.id = id;
		this.equipo = equipo;
		this.fecha = fecha;
		this.serie = serie;
		this.direccion = direccion;
		this.empleado = empleado;
		this.plan = plan;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Equipo getEquipo() {
		return equipo;
	}
	public void setEquipo(Equipo equipo) {
		this.equipo = equipo;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public String getSerie() {
		return serie;
	}
	public void setSerie(String serie) {
		this.serie = serie;
	}
	public Empleado getEmpleado() {
		return empleado;
	}
	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}
	public Plan getPlan() {
		return plan;
	}
	public void setPlan(Plan plan) {
		this.plan = plan;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
}
