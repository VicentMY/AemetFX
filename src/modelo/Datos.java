package modelo;

public class Datos {

	// Atributos
		private int maxima;
		private int minima;
		private double media;
		private int[] valores;

		// Constructor
		public Datos(int maxima, int minima, int[] valores) {

			this.maxima = maxima;
			this.minima = minima;
			this.valores = valores;
			this.media = calcularMedia();
		}

		// Getters
		public int getMaxima() {
			return maxima;
		}

		public int getMinima() {
			return minima;
		}	

		public double getMedia() {
			return media;
		}


		// Métodos
		private double calcularMedia() {

			double suma = 0;
			double media = 0;
			
			for (int i = 0; i < this.valores.length; i++) {
				
				suma += this.valores[i];
			}
			
			media = suma / this.valores.length;
			
			return media;
		}

		// Métodos sobreescritos
		@Override
		public String toString() {
			
			return String.format("%nmáxima -> %d %n"
							   + "mínima -> %d %n"
							   + "media  -> %.2f %n",
							   this.maxima, this.minima, this.media);
		}
}
