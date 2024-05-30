/*
 *    Copyright 2021-2024 Matt Malec, and the Pterodactyl4J contributors
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 * 
 *    ============================================================================== 
 * 
 *    Copyright 2024 RaftDev, and the Pelican4J contributors
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package be.raft.pelican.client.ws.hooks;

import be.raft.pelican.client.ws.events.*;
import be.raft.pelican.client.ws.events.connection.*;
import be.raft.pelican.client.ws.events.error.DaemonErrorEvent;
import be.raft.pelican.client.ws.events.error.JWTErrorEvent;
import be.raft.pelican.client.ws.events.install.InstallCompletedEvent;
import be.raft.pelican.client.ws.events.install.InstallEvent;
import be.raft.pelican.client.ws.events.install.InstallStartedEvent;
import be.raft.pelican.client.ws.events.output.ConsoleOutputEvent;
import be.raft.pelican.client.ws.events.output.DaemonMessageEvent;
import be.raft.pelican.client.ws.events.output.InstallOutputEvent;
import be.raft.pelican.client.ws.events.output.OutputEvent;
import be.raft.pelican.client.ws.events.token.TokenEvent;
import be.raft.pelican.client.ws.events.token.TokenExpiredEvent;
import be.raft.pelican.client.ws.events.token.TokenExpiringEvent;
import be.raft.pelican.client.ws.events.transfer.TransferLogEvent;
import be.raft.pelican.client.ws.events.transfer.TransferStatusEvent;

public abstract class ClientSocketListenerAdapter implements ClientSocketListener {

	public void onStatusUpdate(StatusUpdateEvent event) {}

	public void onStatsUpdate(StatsUpdateEvent event) {}

	public void onAuthSuccess(AuthSuccessEvent event) {}

	public void onOutput(OutputEvent event) {}

	public void onConsoleOutput(ConsoleOutputEvent event) {}

	public void onInstallOutput(InstallOutputEvent event) {}

	public void onDaemonMessage(DaemonMessageEvent event) {}

	public void onInstallUpdate(InstallEvent event) {}

	public void onInstallStarted(InstallStartedEvent event) {}

	public void onInstallCompleted(InstallCompletedEvent event) {}

	public void onTransferLog(TransferLogEvent event) {}

	public void onTransferStatusUpdate(TransferStatusEvent event) {}

	public void onBackupCompleted(BackupCompletedEvent event) {}

	public void onConnectionUpdate(ConnectionEvent event) {}

	public void onConnected(ConnectedEvent event) {}

	public void onDisconnecting(DisconnectingEvent event) {}

	public void onDisconnected(DisconnectedEvent event) {}

	public void onFailure(FailureEvent event) {}

	public void onDaemonError(DaemonErrorEvent event) {}

	public void onJWTError(JWTErrorEvent event) {}

	public void onTokenUpdate(TokenEvent event) {}

	public void onTokenExpiring(TokenExpiringEvent event) {}

	public void onTokenExpired(TokenExpiredEvent event) {}

	public void onGenericEvent(Event event) {}

	@Override
	public final void onEvent(Event event) {
		onGenericEvent(event);
		if (event instanceof StatusUpdateEvent) onStatusUpdate((StatusUpdateEvent) event);
		else if (event instanceof StatsUpdateEvent) onStatsUpdate((StatsUpdateEvent) event);
		else if (event instanceof AuthSuccessEvent) onAuthSuccess((AuthSuccessEvent) event);
		else if (event instanceof ConsoleOutputEvent) onConsoleOutput((ConsoleOutputEvent) event);
		else if (event instanceof InstallOutputEvent) onInstallOutput((InstallOutputEvent) event);
		else if (event instanceof DaemonMessageEvent) onDaemonMessage((DaemonMessageEvent) event);
		else if (event instanceof TransferLogEvent) onTransferLog((TransferLogEvent) event);
		else if (event instanceof TransferStatusEvent) onTransferStatusUpdate((TransferStatusEvent) event);
		else if (event instanceof BackupCompletedEvent) onBackupCompleted((BackupCompletedEvent) event);
		else if (event instanceof InstallStartedEvent) onInstallStarted((InstallStartedEvent) event);
		else if (event instanceof InstallCompletedEvent) onInstallCompleted((InstallCompletedEvent) event);
		else if (event instanceof ConnectedEvent) onConnected((ConnectedEvent) event);
		else if (event instanceof DisconnectingEvent) onDisconnecting((DisconnectingEvent) event);
		else if (event instanceof DisconnectedEvent) onDisconnected((DisconnectedEvent) event);
		else if (event instanceof FailureEvent) onFailure((FailureEvent) event);
		else if (event instanceof DaemonErrorEvent) onDaemonError((DaemonErrorEvent) event);
		else if (event instanceof JWTErrorEvent) onJWTError((JWTErrorEvent) event);
		else if (event instanceof TokenExpiringEvent) onTokenExpiring((TokenExpiringEvent) event);
		else if (event instanceof TokenExpiredEvent) {
			onTokenExpired((TokenExpiredEvent) event);
		}

		if (event instanceof OutputEvent) onOutput((OutputEvent) event);

		if (event instanceof InstallEvent) onInstallUpdate((InstallEvent) event);

		if (event instanceof ConnectionEvent) onConnectionUpdate((ConnectionEvent) event);

		if (event instanceof TokenEvent) onTokenUpdate((TokenEvent) event);
	}
}
