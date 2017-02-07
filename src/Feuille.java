
public class Feuille {
	private int pos;
	private int tab[];
	private Feuille feuilleG;
	private Feuille feuilleD;
	private float poids;
	private float valeur;
	private float borneSup;
	private static float borneInf;

	public Feuille(int t[], int i, int max, boolean estFG){
		this.pos = i;

		//si c'est une feuille gauche on rajoute un element
		if(estFG){	
			// On initie le tableau a la taille du tableau precedent + 1
			this.tab = new int[t.length + 1];

			// Le tableau vaut toutes les valeurs de l'ancien tableau 
			for(int j = 0; j < t.length; ++j)
				this.tab[j] = t[j];
			
			this.tab[this.tab.length - 1] = this.pos;
		}
		//si c'est une feuille droite on réatribue l'ancien tableau sans modification
		else
			this.tab = t;

		// On initialise les variables
		this.setValeur(0);
		this.borneSup = 0;
		Feuille.borneInf = 0;

		//condition d'arret de la recursive
		if(this.pos == max){
			this.setFeuilleG(null);
			this.setFeuilleD(null);
		}
		//recursive (generation des feuilles gauche et droite)
		else{
			this.setFeuilleG(new Feuille(this.tab, this.pos + 1, max, true));
			this.setFeuilleD(new Feuille(this.tab, this.pos + 1, max, false));
		}
	}

	public float getBorneInf(){
		return Feuille.borneInf;
	}

	public void setBorneInf(float b){
		Feuille.borneInf = b;
	}

	public float getBorneSup(){
		return this.borneSup;
	}

	public void setBorneSup(float b){
		this.borneSup = b;
	}

	public float getPoids() {
		return this.poids;
	}

	public void setPoids(float p) {
		this.poids = p;
	}

	public float getValeur() {
		return this.valeur;
	}

	public void setValeur(float v) {
		this.valeur = v;
	}
	
	public int getPos(){
		return this.pos;
	}

	public Feuille getFeuilleG() {
		return this.feuilleG;
	}

	private void setFeuilleG(Feuille f) {
		this.feuilleG = f;
	}

	public int[] getTab(){
		return this.tab;
	}
	
	public Feuille getFeuilleD() {
		return this.feuilleD;
	}

	private void setFeuilleD(Feuille f) {
		this.feuilleD = f;
	}

	public String afficherTab(){
		String s = "[";

		for(int i = 0; i < this.tab.length; ++i){
			s += this.tab[i] + ";";
		}
		s += "]";

		return s;
	}

	@Override
	public String toString(){
		String s = "";

		if(this.feuilleG == null && this.feuilleD == null){
			s += this.afficherTab() + "\n";
		}
		else{
			s += this.getFeuilleG().toString();
			s += this.getFeuilleD().toString();
		}
		return s;
	}

}




