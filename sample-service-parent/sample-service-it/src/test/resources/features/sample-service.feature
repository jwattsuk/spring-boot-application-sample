Feature: Trade Save Event

  Scenario: Trade Save Event
    Given the following trades
      | INSTRUMENT | COUNTERPARTY | NOTIONAL | BUYSELL | CURRENCY |
      | SWAP       | GOLDMANSACHS | 100000   | BUY     | USD      |
      | CDS        | JPMORGAN     | 200000   | SELL    | EUR      |
    When the trades are persisted
    Then all trades can be retrieved
