package vn.mekosoft.designpattern.singleton;

public class SingletonKey {
	//1-Tao moi instance PRIVATE STATIC
	//7-De tranh truong hop INSTANCE bi luu tren bo nho tam, dan den viec init chua hoan thien -> Luu tren Main Memmory
	//bang keyword la volatile
	public static volatile SingletonKey INSTANCE;
	
	//2- Set ham khoi tao la private nham ngan chan viec tao doi tuong moi
	private SingletonKey() {
	}
	//3-Tao ham de lay doi tuong
	public static SingletonKey getInstance() {
		//6-De tranh truong hop luon luon synchronized den den nghen -> Check truoc khi sync
		if(INSTANCE==null) {
			//5-Dong bo lop SingletonKey de dam bao la doi tuong nay chua duoc init boi Thread khac, truoc khi thuc hien init
			synchronized(SingletonKey.class) {
				//4-Kiem tra doi tuong va khoi tao doi tuong
				if(INSTANCE==null) {
					INSTANCE = new SingletonKey();
				}
			}
		}
		return INSTANCE;
	}
	public void openDoor() {
		System.out.println("Opening the door");
	}
}
