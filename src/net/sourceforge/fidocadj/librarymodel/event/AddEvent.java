package net.sourceforge.fidocadj.librarymodel.event;

public class AddEvent
{
	private Object addedNode;
	private Object parentNode;

	public AddEvent(Object parentNode,Object addedNode)
	{
		this.parentNode = parentNode;
		this.addedNode = addedNode;
	}
	
	/**
	 * Returns the value of renamedNode.
	 */

	public Object getAddedNode() {
		return addedNode;
	}

	/**
	 * Returns the value of parentNode.
	 */

	public Object getParentNode() {
		return parentNode;
	}
}