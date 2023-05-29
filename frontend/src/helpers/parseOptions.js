export const parseOptions = ( options = [], optionsVoted = [] ) => {
    const parsedOptions = options.map( (option, index) => (
        {
            ...option,
            id: index,
            selected: optionsVoted.some( optionVotedId => optionVotedId === index ),
            start: new Date( option.start ),
            end: new Date( option.end )
        }
    ))

    return parsedOptions;
}
