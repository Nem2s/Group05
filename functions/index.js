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
  if (!event.data.val()) {
    return console.log('User ', uid, 'does not belong to', gid);
  }
 // console.log('We have a new follower UID:', followerUid, 'for user:', followerUid);

  // Get the list of device notification tokens.
  const getDeviceTokensPromise = admin.database().ref('/users/'+uid+'/fcmToken').once('value');

  // Get the follower profile.
  const getFollowerProfilePromise = admin.database().ref('/groups/'+gid+'/name').once('value');
 console.log('UID: ','/users/'+uid+'/fcmToken');
 console.log('GID: ',gid);

 
 
  return Promise.all([getDeviceTokensPromise, getFollowerProfilePromise]).then(results => {
    const tokensSnapshot = results[0].val();
	const groupName   = results[1].val();

    // Check if there are any device tokens.
   
    console.log('There are ', tokensSnapshot, 'tokens to send notifications to.',groupName);
    //console.log('Fetched follower profile', follower);

    // Notification details.
    const payload = {
      notification: {
        title: groupName +' is created',
        body: 'You have added to a new group',
		sound: 'default',
		badge: '1',
		collapse_key : 'newGroup'
      },
	  data: { 
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
  */
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
		collapse_key : 'newExpense'
      },
	  data: { 
		  groupId:gid
		  
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




exports.sendMessageNotification = functions.database.ref('/chats/{gId}/{mId}').onWrite(event => {
  
  const gid = event.params.gId;
  const message = event.data.val();
  
  // If un-follow we exit the function.	
  if (!event.data.val()) {
    return console.log('User ', uid, 'does not belong to', gid);
  }
  
  const message_content = event.data.val();
  /*
  if(event.previous.val()){
	  return;
  }
  */
 // console.log('We have a new follower UID:', followerUid, 'for user:', followerUid);

  // Get the list of device notification tokens.
  
  //var obj = Object.values(expense);
  console.log(uid,'       ', gid,'     ', eid);
  
  
  
  
  
  
  
  const getGroupProfilePromise = admin.database().ref('/groups/'+gid).once('value');
  
  
 
  return Promise.all([ getGroupProfilePromise]).then(results => {
    const group = results[0].val();
	
	
    // Check if there are any device tokens.
   
    console.log('There are ', tokensSnapshot, 'tokens to send notifications to.',groupName);
    //console.log('Fetched follower profile', follower);

    // Notification details.
    const payload = {
      notification: {
        title: group.name,
        body: message.messageUser+': '+message.messageText,
		sound: 'default',
		badge: '1',
		collapse_key : 'newMessage'
      },
	  data: { 
		  groupId:gid
		  
	  }
	};
	  const tokens = Object.keys(group.members);
	  var promise = new Array();
	  tokens.forEach(function(t){
		  promise.push(admin.database().ref('/users/'+t+'/fcmToken').once('value'));
	  }
	  return Promise.all([promise]).then(results => {
		 const token;
		 results.forEach(function(f){
			 token.push(f.val());
			
		 });
		 
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