package nl.utwente.group10.ui.components.anchors;

import java.util.Optional;

import edu.emory.mathcs.backport.java.util.Arrays;
import nl.utwente.group10.haskell.expr.Expr;
import nl.utwente.group10.haskell.expr.Ident;
import nl.utwente.group10.haskell.type.Type;
import nl.utwente.group10.ui.BackendUtils;
import nl.utwente.group10.ui.CustomUIPane;
import nl.utwente.group10.ui.components.blocks.Block;
import nl.utwente.group10.ui.components.blocks.DisplayBlock;
import nl.utwente.group10.ui.components.blocks.FunctionBlock;
import nl.utwente.group10.ui.components.blocks.InputBlock;
import nl.utwente.group10.ui.components.lines.Connection;
import nl.utwente.group10.ui.exceptions.TypeUnavailableException;
import nl.utwente.group10.ui.handlers.AnchorHandler;

/**
 * Anchor that specifically functions as an input.
 */
public class InputAnchor extends ConnectionAnchor {
    /**
     * @param block The Block this anchor is connected to.
     * @param pane The parent pane this Anchor resides on.
     */
    public InputAnchor(Block block, CustomUIPane pane) {
        super(block, pane);
        new AnchorHandler(pane.getConnectionCreationManager(), this);
    }

    /**
     * @return The expression carried by the connection connected to this
     *         anchor.
     */
    public final Expr asExpr() {
        if (isPrimaryConnected()) {
            return getPrimaryOppositeAnchor().get().getBlock().asExpr();
        } else {
            return new Ident("undefined");
        }
    }

    @Override
    public boolean canAddConnection() {
        // InputAnchors only support 1 connection;
        return !hasConnection();
    }

    @Override
    public String toString() {
        return "InputAnchor for " + getBlock();
    }

    @Override
    public Type getType() {
        if (getBlock() instanceof InputBlock) {
            return ((InputBlock) getBlock()).getInputType(this);
        } else {
            throw new TypeUnavailableException();
        }
    }
}
