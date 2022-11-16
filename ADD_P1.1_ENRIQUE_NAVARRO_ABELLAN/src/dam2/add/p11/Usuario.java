package dam2.add.p11;

public class Usuario {

	private String nombre;
	private String pass;
	private int intentos;
	private boolean bloqueado;
	
	public Usuario() {
		super();
	}
	public Usuario(String nombre, String pass) {
		super();
		this.nombre = nombre;
		this.pass = pass;
	}
	public Usuario(String nombre, int intentos, boolean bloqueado) {
		super();
		this.nombre = nombre;
		this.intentos = intentos;
		this.bloqueado = bloqueado;
	}
	public Usuario(String nombre, String pass, int intentos, boolean bloqueado) {
		super();
		this.nombre = nombre;
		this.pass = pass;
		this.intentos = intentos;
		this.bloqueado = bloqueado;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public int getIntentos() {
		return intentos;
	}
	public void setIntentos(int intentos) {
		this.intentos = intentos;
	}
	public boolean isBloqueado() {
		return bloqueado;
	}
	public void setBloqueado(boolean bloqueado) {
		this.bloqueado = bloqueado;
	}
}
