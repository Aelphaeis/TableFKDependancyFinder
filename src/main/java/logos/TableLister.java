package logos;

import java.util.ArrayDeque;
import java.util.Deque;

import org.apache.log4j.Logger;

import jmo.patterns.visitor.Visitor;
import jmo.structures.TreeNode;
import pojo.TableDependencyInfo;

/**
 * This does not work as intended
 * @author morain
 *
 */
public class TableLister implements Visitor<TreeNode<TableDependencyInfo>>{
	
	private static final Logger logger = Logger.getLogger(TableLister.class);

	Deque<String> out;
	public TableLister() {
		out = new ArrayDeque<>();
	}

	@Override
	public void visit(TreeNode<TableDependencyInfo> element) {
		out.push(element.getValue().getTableName());
	}
	
	public void print(){
		for(String tabName : out){
			logger.info("#"+tabName);
		}
	}

}
