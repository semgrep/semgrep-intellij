"use strict";
Object.defineProperty(exports, "__esModule", {value: true});
exports.LSFactory = void 0;
const LSWasm = require("./language-server-wasm");
const LSFactory = async () => {
    const loadedLSWasm = await LSWasm();
    // libpcre regrettably must be global because semgrep eagerly compiles regexes
    globalThis.LibPcreModule = loadedLSWasm;
    const {init, handleClientMessage, setWriteRef} = require("./Main.bc");
    init(loadedLSWasm);
    const clientMessageHandler = async (packet) => {
        const json = JSON.stringify(packet);
        const result = await handleClientMessage(json);
        if (result) {
            // Parse LSP Response and get result
            return JSON.parse(result)["result"];
        }
        return undefined;
    };
    return {
        handleClientMessage: clientMessageHandler,
        setWriteRef: setWriteRef,
    };
};
exports.LSFactory = LSFactory;
//# sourceMappingURL=semgrep-lsp-bindings.js.map