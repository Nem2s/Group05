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
		groupId:gid
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

/*


exports.sendNewExpenseNotification = functions.database.ref('/expenses/{gId}/{eId}/members/{uId}').onWrite(event => {
  const eid = event.params.eId;
  const gid = event.params.gId;
  const uid = event.params.uId;
  
  // If un-follow we exit the function.	
  if (!event.data.val()) {
    return console.log('User ', uid, 'does not belong to', gid);
  }
  /*
  if(event.previous.val()){
	  return;
  }
  
 // console.log('We have a new follower UID:', followerUid, 'for user:', followerUid);

  // Get the list of device notification tokens.
  
  //var obj = Object.values(expense);
  console.log(uid,'       ', gid,'     ', eid);
  
  
  
  
  
  
  
  const getDeviceTokensPromise = admin.database().ref('/users/'+uid+'/fcmToken').once('value');
  const getGroupProfilePromise = admin.database().ref('/groups/'+gid+'/name').once('value');
  const getExpenseProfilePromise = admin.database().ref('/expenses/'+gid+'/'+eid).once('value');
  
 
  return Promise.all([getDeviceTokensPromise, getGroupProfilePromise,getExpenseProfilePromise]).then(results => {
    const tokensSnapshot = results[0].val();
	const groupName   = results[1].val();
	const expense = results[2].val();
    // Check if there are any device tokens.
   
    console.log('There are ', tokensSnapshot, 'tokens to send notifications to.',groupName);
    //console.log('Fetched follower profile', follower);

    // Notification details.
    const payload = {
      notification: {
        title:expense.name+' is created ' +'in ' +groupName +' by '+expense.owner,
        body: 'Debit: '+ expense.members[uid],
		sound: 'default',
		badge: '1',
		collapse_key : 'newExpense',
		icon:'idea.png'
		
		
      },
	  data: { 
		  groupId:gid,
		  expenseId:expense.id
		  
		  
	  }
    };
    return admin.messaging().sendToDevice(tokensSnapshot, payload).then(response => {
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

*/


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
  
  const getGroupProfilePromise = admin.database().ref('/groups/'+gid).once('value');
  return Promise.all([ getGroupProfilePromise]).then(results => {
    const group = results[0].val();
	
	
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



