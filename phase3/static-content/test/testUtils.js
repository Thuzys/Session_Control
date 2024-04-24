function testErrorWhenNull(func, arg1, arg2) {
    let errorOccurred = false;
    try {
        func(arg1, arg2);
    } catch (error) {
        errorOccurred = true;
    }
    if (!errorOccurred) {
        throw new Error('Expected function to throw an error, but it did not.');
    }
}

function setupTest(func) {
    const mainContent = document.createElement("div");
    const headerContent = document.createElement("div");
    func(mainContent, headerContent);
    return mainContent.children
}

const testUtils = {
    testErrorWhenNull,
    setupTest,
}

export default testUtils;