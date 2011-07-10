package cern.accsoft.steering.jmad.tools.interpolate;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import cern.accsoft.steering.jmad.domain.elem.Element;
import cern.accsoft.steering.jmad.domain.result.tfs.TfsResult;
import cern.accsoft.steering.jmad.domain.types.enums.JMadPlane;
import cern.accsoft.steering.jmad.domain.var.enums.JMadTwissVariable;

public class OrbitInterpolationResultImpl implements OrbitInterpolationResult {

    /** the collected values for the interpolation per element */
    private Map<Element, Map<JMadPlane, Double>> perElementValues = new HashMap<Element, Map<JMadPlane, Double>>();

    /** the collected values of the interpolation per plane */
    private Map<JMadPlane, Map<Element, Double>> perPlaneValues = null;

    /** the twiss variable the interpolation was performed for */
    private JMadTwissVariable jmadTwissVariable;

    /** the result of the interpolation is just an empty tfs result at the beginning */
    private TfsResult result = new InterpolationTfsResult(JMadTwissVariable.UNKNOWN,
            new HashMap<Element, Map<JMadPlane, Double>>());

    public OrbitInterpolationResultImpl(JMadTwissVariable twissVariable) {
        this.jmadTwissVariable = twissVariable;
    }

    @Override
    public TfsResult getTfsResult() {
        return this.result;
    }

    public void addValuesPerElement(Map<Element, Map<JMadPlane, Double>> valueMapping) {
        perElementValues.putAll(valueMapping);
    }

    public void addValuesPerPlane(JMadPlane plane, Map<Element, Double> valueMapping) {
        if (this.perPlaneValues == null) {
            this.perPlaneValues = new HashMap<JMadPlane, Map<Element, Double>>();
            for (JMadPlane jmadPlane : JMadPlane.values()) {
                this.perPlaneValues.put(jmadPlane, new HashMap<Element, Double>());
            }
        }

        this.perPlaneValues.get(plane).putAll(valueMapping);
    }

    public void create() {
        if (this.perElementValues.size() > 0 && this.perPlaneValues != null) {
            throw new IllegalStateException("Orbit interpolation result MUST either be filled per element "
                    + "or per plane, not both at the same time!");
        }

        if (this.perPlaneValues != null) {
            for (JMadPlane plane : JMadPlane.values()) {
                for (Entry<Element, Double> entry : this.perPlaneValues.get(plane).entrySet()) {
                    Map<JMadPlane, Double> elementValues = this.perElementValues.get(entry.getKey());
                    if (elementValues == null) {
                        elementValues = new HashMap<JMadPlane, Double>();
                        this.perElementValues.put(entry.getKey(), elementValues);
                    }

                    elementValues.put(plane, entry.getValue());
                }
            }
        }

        this.result = new InterpolationTfsResult(this.jmadTwissVariable, this.perElementValues);
    }
}