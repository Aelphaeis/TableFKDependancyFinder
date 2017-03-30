package logos;

import java.util.Stack;

import pojo.TableDependencyInfo;
import structures.TreeNode;
import structures.Visitor;

public class TableLister implements Visitor<TreeNode<TableDependencyInfo>>{

	Stack<String> out;
	public TableLister() {
		out = new Stack<String>();
	}

	@Override
	public void visit(TreeNode<TableDependencyInfo> element) {
		out.push(element.getValue().getTableName());
	}
	
	public void print(){
		for(String tabName : out){
			System.out.println("#"+tabName);
		}
	}

}
