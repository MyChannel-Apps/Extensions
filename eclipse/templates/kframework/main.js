/**
	The MIT License (MIT)

	Copyright (c) 2014 MyChannel-Apps.de

	Permission is hereby granted, free of charge, to any person obtaining a copy
	of this software and associated documentation files (the "Software"), to deal
	in the Software without restriction, including without limitation the rights
	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
	copies of the Software, and to permit persons to whom the Software is
	furnished to do so, subject to the following conditions:

	The above copyright notice and this permission notice shall be included in
	all copies or substantial portions of the Software.

	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
	THE SOFTWARE.
	
	@author		Adrian Preuß, Bizarrus
*/

require('framework/KFramework.js');

var App = (new function() {
	/* App Events */
	this.onAppStart = function() {
		KFramework.startUp();
		
		// @ToDo auto-generated
	};
	
	this.onPrepareShutdown = function(secondsTillShutdown) {
		// @ToDo auto-generated
	};
	
	this.onShutdown = function() {
		KFramework.shutDown();
		
		// @ToDo auto-generated
	};
	
	/* User Events */
	this.onUserJoined = function(user) {
		// @ToDo auto-generated
	};
	
	this.onUserLeft = function(user) {
		// @ToDo auto-generated
	};
	
	/* Access Events */
	this.mayJoinChannel = function(user) {
		// @ToDo auto-generated
	};
	
	/* Message Events */
	this.maySendPublicMessage = function(publicMessage) {
		// @ToDo auto-generated
	};
	
	this.onPrivateMessage = function(privateMessage) {
		// @ToDo auto-generated
	};
	
	this.onPublicMessage = function(publicMessage) {
		// @ToDo auto-generated
	};
	
	/* Knuddel Events */
	this.onKnuddelReceived = function(sender, receiver, knuddelAmount) {
		// @ToDo auto-generated
	};
	
	/* DICE Events */
	this.onUserDiced = function(diceEvent) {
		// @ToDo auto-generated
	};
	
	/* Chat Commands */
	this.chatCommands = {
		// @ToDo auto-generated
	};
}());