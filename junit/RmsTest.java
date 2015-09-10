import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;

import junit.framework.TestCase;

/*
 * Created on Mar 11, 2007 by Stefan Haustein
 */

public class RmsTest extends TestCase {
	
	RecordStore store;
	
	
	public RmsTest(String s){
		super(s);
	}
	
	/**
	 * the count of the stores does not go up to take account of the last
	 * created store until a call of addRecord!!
	 *
	 *  @author Benedict Heal
	 *
	 * <pre>
	 *  	 --------------- RMSTest
testCreateRecordStoreDoesNotChangeStoreCountUntilAddRecord
	 *  	 record store
name:rmsForTesting-testCreateRecordStoreDoesNotChangeStoreCountUntilAddRecord1161192914796
	 *  	 RecordStore.openRecordStore(rmsForTesting-testCreateRecordStoreDoesNotChangeStoreCountUntilAddRecord1161192914796,
true);
	 *  	 creating store changes store count 9 to 9
	 *  	 leaving initRecordStore    store count: 9
	 *  	 before addRecord  	    store count: 9
	 *  	 after addRecord	    store count: 10
	 *  	 adding a record changed store count 9 to 10
	 *       	
	 * </pre>
	 *
	 */
	public void testCreateRecordStoreDoesNotChangeStoreCountUntilAddRecord()
			throws Exception {

		createStoreForTest();
		
		String dataString = "abc";
		byte[] dataBytes = dataString.getBytes();
		int offset = 0;
		int count = dataBytes.length;

		int preStoreCount = RecordStore.listRecordStores().length;

		int preRecordCount = store.getNumRecords();

//		dumpRmsState("before addRecord  	");
		store.addRecord(dataBytes, offset, count);
//		dumpRmsState("after addRecord	");

		int postStoreCount = RecordStore.listRecordStores().length;

		int postRecordCount = store.getNumRecords();
		assertEquals("wrong record count", 1 + preRecordCount, postRecordCount);

		System.out.println("adding a record changed store count "
				+ preStoreCount + " to " + postStoreCount);


	}

	private void createStoreForTest() throws  RecordStoreException {
		try{
			RecordStore.deleteRecordStore("TestRecordStore");
		}
		catch(RecordStoreException e){
		}
		
		store = RecordStore.openRecordStore("TestRecordStore", true);
	}
	
	
}
