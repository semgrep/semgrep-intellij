"use strict";
Object.defineProperty(exports, "__esModule", {value: true});
/*
 * This file connects LSP.js and VSCode's LSP api together
 *
 * We could manage all IO related things, like reading from stdin and writing to stdout, here
 * but since we're already in JS lang, let's just use Microsofts Official TM LSP library.
 *
 * this allows us to use this file through node-ipc which is the defacto way to do this stuff
 *
 */
const semgrep_lsp_bindings_1 = require("./semgrep-lsp-bindings");
const node_1 = require("vscode-languageserver/node");
const connection = (0, node_1.createConnection)();
var server = undefined;
var idCntr = 0;
connection.onInitialize(async (params) => {
    // this is kind of gross to do this here, but it's simple.
    server = await (0, semgrep_lsp_bindings_1.LSFactory)();
    // Tell the LS to use this function to write to stdout
    server.setWriteRef((json) => {
        // Parse JSON from ocaml code
        const packet = JSON.parse(json);
        // extract method and params since that's what the LSP expects
        const method = packet.method;
        const params = packet.params;
        // Send the notification to the client
        connection.sendNotification(method, params);
    });
    // surely there's a better way
    if (!params.initializationOptions) {
        params.initializationOptions = {};
    }
    if (!params.initializationOptions.scan) {
        params.initializationOptions.scan = {};
    }
    // Force 1 job and no timeout
    // since these require unix primitives
    params.initializationOptions.scan.jobs = 1;
    params.initializationOptions.scan.timeout = 0;
    const response = await server.handleClientMessage({
        jsonrpc: "2.0",
        method: "initialize",
        params: params,
        id: idCntr++,
    });
    return response;
});
connection.onRequest(async (method, params) => {
    const json = {jsonrpc: "2.0", method: method, params: params, id: idCntr++};
    if (server) {
        const response = await server.handleClientMessage(json);
        return response;
    }

});
connection.onNotification((method, params) => {
    const json = {jsonrpc: "2.0", method: method, params: params};
    if (server) {
        server.handleClientMessage(json);
    }
});
connection.onShutdown(() => {
    if (server) {
        server.handleClientMessage({
            jsonrpc: "2.0",
            method: "shutdown",
            params: null,
            id: idCntr++,
        });
    }
});
// Let's go!
connection.listen();
//# sourceMappingURL=semgrep-lsp.js.map