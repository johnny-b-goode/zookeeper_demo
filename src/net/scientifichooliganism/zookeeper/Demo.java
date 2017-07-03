package net.scientifichooliganism.zookeeper;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooDefs;

public class Demo {
	private ZooKeeper zk;
	private String rootNode;

	public Demo () {
//		rootNode = "/" + this.getClass().getName();
		rootNode = "/net.scientifichooliganism.zookeeper";

		try {
			zk = new ZooKeeper("127.0.0.1:2181", 2000, null);

			if (zk.exists(getRootNode(), false) == null) {
				zk.create(getRootNode(), "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			}
		}
		catch (Exception exc) {
			exc.printStackTrace();
			zk = null;
		}
	}

	public void createNode (String key, String value) {
		if (key == null || value == null) {
			throw new IllegalArgumentException("createNode(String, String) String is null");
		}

		if (key.isEmpty()) {
			throw new IllegalArgumentException("createNode(String, String) String is empty");
		}

		try {
			zk.create((getRootNode() + "/" + key), value.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		}
		catch (Exception exc) {
			exc.printStackTrace();
		}
	}

	public String getValue (String key) {
		String ret = new String();

		try {
			ret = new String(zk.getData((getRootNode() + "/" + key), false, zk.exists((getRootNode() + "/" + key), false)));
		}
		catch (Exception exc) {
			exc.printStackTrace();
		}

		return ret;
	}

	public void dumpConfigs () {
		try {
			for (String item : zk.getChildren(getRootNode(), false)) {
				System.out.println("	" + item + ": " + getValue(item));
			}
		}
		catch (Exception exc) {
			exc.printStackTrace();
		}
	}

	public ZooKeeper getZooKeeper () {
		return zk;
	}

	public String getRootNode () {
		return rootNode;
	}

	public static void main (String [] args) {
		try {
			System.out.println("starting demo...");
			Demo d = new Demo();
			System.out.println("creating configs...");
			d.createNode("key-01", "value-01");
			d.createNode("key-02", "value-02");
			d.createNode("key-03", "value-03");
			d.createNode("key-04", "value-04");
			System.out.println("dumping configs...");
			d.dumpConfigs();
			System.out.println("ending demo");
		}
		catch (Exception exc) {
			exc.printStackTrace();
		}
		finally {
			System.exit(0);
		}
	}
}