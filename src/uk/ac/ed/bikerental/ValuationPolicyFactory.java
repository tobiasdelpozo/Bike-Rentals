package uk.ac.ed.bikerental;

public class ValuationPolicyFactory {
    private static ValuationPolicy valuationPolicyInstance;

    public static ValuationPolicy getValuationPolicy() {
        if (valuationPolicyInstance == null) {
            // Not implemented -- we are only interested in testing using the Mock.
            assert false;
        }
        return valuationPolicyInstance;
    }

    public static void setupMockValuationPolicyService() {
        // Should only be called in unit tests, not production code.
        valuationPolicyInstance = new MockValuationPolicy();
    }
}
