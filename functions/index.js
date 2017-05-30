/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
'use strict';

const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

/**
 * Triggers when a user gets a new follower and sends a notification.
 *
 * Followers add a flag to `/followers/{followedUid}/{followerUid}`.
 * Users save their device notification tokens to `/users/{followedUid}/notificationTokens/{notificationToken}`.
 */
exports.sendNewGroupNotification = functions.database.ref('/users/{uId}/userGroups/{gId}').onWrite(event => {
  const uid = event.params.uId;
  const gid = event.params.gId;
  // If un-follow we exit the function.	
  console.log(event.data.previous.exists());
   if (event.data.previous.exists()) {
        return;
      }
      // Exit when the data is deleted.
      if (!event.data.exists()) {
        return;
      }
 // console.log('We have a new follower UID:', followerUid, 'for user:', followerUid);

  // Get the list of device notification tokens.
  const getDeviceTokensPromise = admin.database().ref('/users/'+uid+'/fcmToken').once('value');

  // Get the follower profile.
  const getFollowerProfilePromise = admin.database().ref('/groups/'+gid).once('value');
 console.log('UID: ','/users/'+uid+'/fcmToken');
 console.log('GID: ',gid);

 
 
  return Promise.all([getDeviceTokensPromise, getFollowerProfilePromise]).then(results => {
    const tokensSnapshot = results[0].val();
	const group   = results[1].val();
	const groupName = group.name
    // Check if there are any device tokens.
 if(uid==group.creator){
	   console.log('Creator: ',uid);
	   return;
   }
    //console.log('Fetched follower profile', follower);

    // Notification details.
    const payload = {
  /*    notification: {
        title: groupName +' is created',
        body: 'You have added been to a new group',
		sound: 'default',
		badge: '1',
		collapse_key : 'newGroup'
      },*/
	  data: { 
		groupName:groupName,
		type:"newGroup",
		groupId:gid,
		icon:'/groups/'+gid+'/'+group.pictureUrl
	  }
	  
    };

    // Listing all tokens.
    

    // Send notifications to all tokens.
    return admin.messaging().sendToDevice(tokensSnapshot, payload).then(response => {
      // For each message check if there was an error.
      const tokensToRemove = [];
      response.results.forEach((result, index) => {
        const error = result.error;
        if (error) {
          console.error('Failure sending notification to', tokensSnapshot, error);
          
        }
      });
      return Promise.all(tokensToRemove);
    });
  });
});



exports.sendMessageNotification = functions.database.ref('/chats/{gId}/{mId}').onWrite(event => {
  
  const gid = event.params.gId;
  const message = event.data.val();
  const uid = message.messageUserId;
  
  // If un-follow we exit the function.	
  if (event.data.previous.exists()) {
        return;
      }
      // Exit when the data is deleted.
      if (!event.data.exists()) {
        return;
      }
  const getDeviceTokensPromise = admin.database().ref('/users/'+uid+'/userInfo').once('value');
  const getGroupProfilePromise = admin.database().ref('/groups/'+gid).once('value');
  return Promise.all([ getGroupProfilePromise,getDeviceTokensPromise]).then(results => {
    const group = results[0].val();
    const sender = results[1].val();
	
	
    // Check if there are any device tokens.
   
    console.log('There are ', 'tokens to send notifications to.',group.name);
    //console.log('Fetched follower profile', follower);

    // Notification details.
    const payload = {
      /*notification: {
        title: group.name,
		icon:'chat_message_arrow',
        body: message.messageUser+': '+message.messageText,
		sound: 'default',
		badge: '1',
		collapse_key : 'newMessage'
      },*/
	  data: {
		  type:"newMessage",
		  message:message.messageText,
		  messageUser:message.messageUser,
		  icon:'/users/'+uid+'/'+sender.iProfile,
		  groupName:group.name,
		  groupId:gid
		  
	  }
	};
	  const tokens = Object.keys(group.members);
	  const promise = new Array();
	  tokens.forEach(function(t){
		  promise.push(admin.database().ref('/users/'+t).once('value'));
	  });
	  return Promise.all(promise).then(results => {
		
		 const token= new Array();
		 results.forEach(function(f){
			 const userrr= f.val();
			 const map = userrr.userInfo;
			 console.log(map["id"]!=uid,map["name"],uid,map["id"]);
			 const map_group=userrr.userGroups;
			 if(map["id"]!=uid && map_group[gid])
				token.push(userrr.fcmToken);
			
		 });
		 
		  return admin.messaging().sendToDevice(token, payload).then(response => {
      const tokensToRemove = [];
      response.results.forEach((result, index) => {
        const error = result.error;
        if (error) {
          console.error('Failure sending notification to', token[index], error);
        }
      });
      return Promise.all(tokensToRemove);
    });
  });
});
});



exports.sendNewExpenseNotification = functions.database.ref('/expenses/{gId}/{eId}').onWrite(event => {
  
  const gid = event.params.gId;
  const eid = event.params.eId;
  
  // If un-follow we exit the function.	
 if (event.data.previous.exists()) {
        return;
      }
      // Exit when the data is deleted.
      if (!event.data.exists()) {
        return;
      }
  
  const expense_content = event.data.val();
const map_debit = expense_content.members;

  //console.log(uid,'       ', gid,'     ', eid);
  const getGroupProfilePromise = admin.database().ref('/groups/'+gid).once('value');
  const getOwnerPromise = admin.database().ref('/users/'+expense_content.owner+'/userInfo').once('value');
  return Promise.all([ getGroupProfilePromise,getOwnerPromise]).then(results => {
    const group = results[0].val();
	const owner = results[1].val();
    // Check if there are any device tokens.
   
    //console.log('There are ', 'tokens to send notifications to.',group.name);
    //console.log('Fetched follower profile', follower);

    // Notification details.
 
	  const tokens = Object.keys(group.members);
	  const promise = new Array();
	  
	  tokens.forEach(function(t){
		  if(t!=expense_content.owner) 
		  promise.push(admin.database().ref('/users/'+t).once('value'));
	 // console.log(t,group.members)
	  });
	  return Promise.all(promise).then(results => {
		// const token= new Array();
		 results.forEach(function(f){
			 const user_tmp = f.val();
			 const map = user_tmp.userGroups;
			 //console.log(group.name,',',expense_content.name,',',map,user_tmp.userInfo.name);
			 if(map[gid]){
			const token=user_tmp.fcmToken;
			const debit = map_debit[user_tmp.userInfo.id];
			   const payload = {
									data: { 
											groupId:gid,
											expense_owner:owner.name,
											icon:'/users/'+owner.id+'/'+owner.iProfile,
											expense_name:expense_content.name,
											groupName:group.name,
											expenseId:eid,
											type:"newExpense",
											expense_debit:debit.toString()
							
										}
								};
						 admin.messaging().sendToDevice(token, payload);
					}
		 });
		// console.log(token);
		/* return admin.messaging().sendToDevice(token, payload).then(response => {
      const tokensToRemove = [];
      response.results.forEach((result, index) => {
        const error = result.error;
        if (error) {
          console.error('Failure sending notification to', token[index], error);
        }
      });
      return Promise.all(tokensToRemove);
    });
	*/
  });
});
});

exports.sendPaymentNotification = functions.database.ref('/history/{gId}/{eId}/notify/{uId}').onWrite(event => {
  
  const gid = event.params.gId;
  const uid =  event.params.uId;
  const eid =  event.params.eId;
  const notified =  event.data.val();
  
  // If un-follow we exit the function.	
 /* if (event.data.previous.exists()) {
        return;
      }*/
	  
      // Exit when the data is deleted.
      if (!event.data.exists() || !notified) {
        return;
      }
  
  const getGroupProfilePromise = admin.database().ref('/groups/'+gid).once('value');
  const getExpenseProfilePromise = admin.database().ref('/expenses/'+gid+'/'+eid).once('value');
  //const getExpenseProfilePromise = admin.database().ref('/expenses/'+gid).once('value');
  const getUserProfilePromise = admin.database().ref('/users/'+uid).once('value');
  const getHistoryProfilePromise = admin.database().ref('/expenses/'+gid+'/'+eid).once('value');
  
  return Promise.all([ getGroupProfilePromise,getExpenseProfilePromise,getUserProfilePromise,getHistoryProfilePromise]).then(results => {
    const group = results[0].val();
	const expense = results[1].val();
    const user = results[2].val();
   // const history = results[3].val();
	//console.log(history);
	if(expense.payed!=null){
					const map_history= expense.payed;
						if(map_history[uid]!=null)
							if(map_history[uid])
							{admin.database().ref('/history/'+gid+'/'+eid+'/notify/'+uid).remove();
							return;
							}
				}
	
    // Check if there are any device tokens.
   
   // console.log(group,',',expense,',',user);
    //console.log('Fetched follower profile', follower);

    // Notification details.
    const payload = {
	  data: {
		  type:"paymentRequest",
		  groupName:group.name,
		  expenseDebit:expense.members[uid].toString(),
		  expenseName:expense.name,
		  requestFrom:user.userInfo.name,
		  icon:'/users/'+uid+'/'+user.userInfo.iProfile,
		  expenseId:eid,
		  requestFromId:uid,
		  groupId:gid
		  
	  }
	};
	  const promise =admin.database().ref('/users/'+expense.owner).once('value');
	admin.database().ref('/history/'+gid+'/'+eid+'/notify/'+uid).set(false);
	  return Promise.all([promise]).then(results => {
		
		 const result= results[0].val();
		 const token = result.fcmToken;		 
		 console.log(result.userInfo.name);
		 
		return admin.messaging().sendToDevice(token, payload).then(response => {
		const tokensToRemove = [];
		response.results.forEach((result, index) => {
        const error = result.error;
        if (error) {
          console.error('Failure sending notification to', token[index], error);
        }
      });
      return Promise.all(tokensToRemove);
    });
  });
});
});

exports.sendReminderPayment = functions.database.ref('/history/{gId}/{eId}/notifyToAll/{uId}').onWrite(event => {
	
	  const gid = event.params.gId;
  const uid =  event.params.uId;
  const eid =  event.params.eId;
  
	
	 if (event.data.previous.exists()) {
        return;
      }
      // Exit when the data is deleted.
      if (!event.data.exists()) {
        return;
      }
	const getGroupProfilePromise = admin.database().ref('/groups/'+gid).once('value');
	const getExpenseProfilePromise = admin.database().ref('/expenses/'+gid+'/'+eid).once('value');
  
	const getUserProfilePromise = admin.database().ref('/users/'+uid).once('value');
	return Promise.all([ getGroupProfilePromise,getExpenseProfilePromise,getUserProfilePromise]).then(results => {
		const group = results[0].val();
	const expense = results[1].val();
	
    const user = results[2].val();
	
	const tokens = Object.keys(expense.members);
	  const promise = new Array();
	  tokens.forEach(function(t){
		 if(t!=uid && !expense.payed[t])
		  promise.push(admin.database().ref('/users/'+t).once('value'));
	  });
	  return Promise.all(promise).then(results => {
		  
		   results.forEach(function(f){
			   const userrr= f.val();
			const token=  userrr.fcmToken;
			  console.log(userrr.userInfo.id);
			   const payload = {
	  data: {
		  type:"rememberPayment",
		  groupName:group.name,
		  expenseDebit:expense.members[userrr.userInfo.id].toString(),
		  expenseName:expense.name,
		  requestFrom:user.userInfo.name,
		  icon:'/users/'+uid+'/'+user.userInfo.iProfile,
		  expenseId:eid,
		  requestFromId:uid,
		  groupId:gid
		  
	  }
	};
		  
		  admin.database().ref('/history/'+gid+'/'+eid+'/notifyToAll/'+uid).remove();
		    admin.messaging().sendToDevice(token, payload).then(response => {
      const tokensToRemove = [];
      response.results.forEach((result, index) => {
        const error = result.error;
        if (error) {
          console.error('Failure sending notification to', token[index], error);
        }
      });
      return Promise.all(tokensToRemove);
    });
		  });
	  });
	
	});
});



