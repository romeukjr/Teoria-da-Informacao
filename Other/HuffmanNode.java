public class HuffmanNode {
	public int Index;
	public char[] Symbol;
	public int Frequency;
	public HuffmanNode Right;
	public HuffmanNode Left;
	
	public HuffmanNode(){}
	
	public HuffmanNode(Integer index) {
		this.Index = index;
	}
	
	public HuffmanNode(char[] symbol){
		this.Symbol = symbol;
		this.Frequency = 1;
	}
	
	public HuffmanNode(char[] symbol, int frequency){
		this.Symbol = symbol;
		this.Frequency = frequency;
	}
	
	public HuffmanNode(String symbol, int frequency){
		this.Symbol = symbol.toCharArray();
		this.Frequency = frequency;
	}
	
	public boolean isLeaf(){
		return ((Symbol.length > 0) && (Symbol[0] != '\u0000') && (Right == null) && (Left == null));
	}
}
