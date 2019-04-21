package assign3;

public class MetropolisInfo {
	private String metropolis;
	private String continent;
	private int population;
	
	public MetropolisInfo(String metropolis, String continent, int population) {
		this.metropolis = metropolis;
		this.continent = continent;
		this.population = population;
	}
	
	public String getMetropolis() {
		return metropolis;
	}
	
	public String getContinent() {
		return continent;
	}
	
	public int getPopulation () {
		return population;
	}
	
}
