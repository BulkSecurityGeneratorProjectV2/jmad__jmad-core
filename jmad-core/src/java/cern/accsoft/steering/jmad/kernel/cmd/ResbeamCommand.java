package cern.accsoft.steering.jmad.kernel.cmd;

import java.util.ArrayList;
import java.util.List;

import cern.accsoft.steering.jmad.domain.beam.Beam;
import cern.accsoft.steering.jmad.kernel.cmd.param.GenericParameter;
import cern.accsoft.steering.jmad.kernel.cmd.param.Parameter;

/**
 * Represents the command in MadX which resets the beam to the default values. (<a
 * href="http://mad.web.cern.ch/mad/Introduction/resbeam.html">RESBEAM</a>).
 * 
 * @author kfuchsbe
 */
public class ResbeamCommand extends AbstractCommand {

    /** The name of this command */
    private static final String CMD_NAME = "resbeam";

    /** The name of the sequence whose beam to reset */
    private final String sequence;

    /**
     * creates a resbeam command, probably associated with a special sequence (defined in beam);
     * 
     * @param beam the beam which to reset
     */
    public ResbeamCommand(Beam beam) {
        sequence = beam.getSequence();
    }

    @Override
    public String getName() {
        return CMD_NAME;
    }

    @Override
    public List<Parameter> getParameters() {
        ArrayList<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new GenericParameter<String>("sequence", sequence));
        return parameters;
    }

    public String getSequence() {
        return sequence;
    }

}