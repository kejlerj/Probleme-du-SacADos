
public class Objet {
	private String nom;
	private float valeur;
	private float poids;
	private float ratio;
	private boolean dansLeSac;

	
	public Objet (String nom, float poids, float valeur){
		this.nom = nom;
		this.valeur = valeur;
		this.poids = poids;
		this.ratio = this.valeur / this.poids;
		this.dansLeSac = false;
	}
	
	public String getNom(){
		return this.nom;
	}
	public float getValeur(){
		return this.valeur;
	}
	public float getPoids(){
		return this.poids;
	}
	
	public float getRatio(){
		return this.ratio;
	}
	
	public boolean getDansLeSac(){
		return this.dansLeSac;
	}
	
	public void mettreDansLeSac(){
		this.dansLeSac = true;
	}
	public void sortirDuSac(){
		this.dansLeSac = false;
	}
}
