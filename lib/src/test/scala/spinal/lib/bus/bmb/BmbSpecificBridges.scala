package spinal.lib.bus.bmb

import org.scalatest.funsuite.AnyFunSuite
import spinal.tester.SpinalSimFunSuite

import spinal.core._
import spinal.core.sim.SimConfig
import spinal.lib.bus.bmb.sim.BmbBridgeTester

class SpinalSimBmbAlignerTester extends AnyFunSuite {
  for(w <- List(false, true); r <- List(false, true);   if w || r) {
    val header = "_" + (if(w) "w" else "") + (if(r) "r" else "")
    test("BmbAligner_bypass" + header) {
      SimConfig.compile {
        val c = BmbAligner(
          ip = BmbParameter(
            addressWidth = 16,
            dataWidth = 32,
            lengthWidth = 6,
            sourceWidth = 4,
            contextWidth = 3,
            canRead = r,
            canWrite = w,
            alignment = BmbParameter.BurstAlignement.WORD
          ),
          alignmentWidth = 2
        )
        c.rework {
          RegNext(True) init (False) setName ("dummy")
        }
        c
      }.doSimUntilVoid("test") { dut =>
        new BmbBridgeTester(
          master = dut.io.input,
          masterCd = dut.clockDomain,
          slave = dut.io.output,
          slaveCd = dut.clockDomain
        )
      }
    }

    test("BmbAligner_4" + header) {
      SimConfig.compile {
        BmbAligner(
          ip = BmbParameter(
            addressWidth = 16,
            dataWidth = 32,
            lengthWidth = 6,
            sourceWidth = 4,
            contextWidth = 3,
            canRead = r,
            canWrite = w,
            alignment = BmbParameter.BurstAlignement.WORD
          ),
          alignmentWidth = 4
        )
      }.doSimUntilVoid("test") { dut =>
        new BmbBridgeTester(
          master = dut.io.input,
          masterCd = dut.clockDomain,
          slave = dut.io.output,
          slaveCd = dut.clockDomain
        )
      }
    }

    test("BmbAligner_3" + header) {
      SimConfig.compile {
        BmbAligner(
          ip = BmbParameter(
            addressWidth = 16,
            dataWidth = 32,
            lengthWidth = 6,
            sourceWidth = 4,
            contextWidth = 3,
            canRead = r,
            canWrite = w,
            alignment = BmbParameter.BurstAlignement.WORD
          ),
          alignmentWidth = 3
        )
      }.doSimUntilVoid("test") { dut =>
        new BmbBridgeTester(
          master = dut.io.input,
          masterCd = dut.clockDomain,
          slave = dut.io.output,
          slaveCd = dut.clockDomain
        )
      }
    }

    test("BmbAligner_1" + header) {
      SimConfig.compile {
        val c = BmbAligner(
          ip = BmbParameter(
            addressWidth = 16,
            dataWidth = 32,
            lengthWidth = 6,
            sourceWidth = 4,
            contextWidth = 3,
            canRead = r,
            canWrite = w,
            alignment = BmbParameter.BurstAlignement.WORD
          ),
          alignmentWidth = 1
        )
        c.rework {
          RegNext(True) init (False) setName ("dummy")
        }
        c
      }.doSimUntilVoid("test") { dut =>
        new BmbBridgeTester(
          master = dut.io.input,
          masterCd = dut.clockDomain,
          slave = dut.io.output,
          slaveCd = dut.clockDomain
        )
      }
    }
  }
}

class SpinalSimBmbLengthFixerTester extends SpinalSimFunSuite {
  test("bypass") {
    SimConfig.compile {
      val c = BmbLengthFixer(
        ip = BmbParameter(
          addressWidth = 16,
          dataWidth = 32,
          lengthWidth = 6,
          sourceWidth = 4,
          contextWidth = 3,
          alignmentMin = 2,
          canRead = true,
          canWrite = true,
          alignment = BmbParameter.BurstAlignement.WORD
        ),
        fixedWidth = 2
      )
      c
    }.doSimUntilVoid("test") { dut =>
      new BmbBridgeTester(
        master = dut.io.input,
        masterCd = dut.clockDomain,
        slave = dut.io.output,
        slaveCd = dut.clockDomain,
        alignmentMinWidth = dut.ip.access.alignmentMin
      )
    }
  }

  test("3") {
    SimConfig.compile {
      val c = BmbLengthFixer(
        ip = BmbParameter(
          addressWidth = 16,
          dataWidth = 32,
          lengthWidth = 6,
          sourceWidth = 4,
          contextWidth = 3,
          alignmentMin = 3,
          canRead = true,
          canWrite = true,
          alignment = BmbParameter.BurstAlignement.WORD
        ),
        fixedWidth = 3
      )
      c
    }.doSimUntilVoid("test") { dut =>
      new BmbBridgeTester(
        master = dut.io.input,
        masterCd = dut.clockDomain,
        slave = dut.io.output,
        slaveCd = dut.clockDomain,
        alignmentMinWidth = dut.ip.access.alignmentMin
      )
    }
  }

  test("4") {
    SimConfig.compile {
      val c = BmbLengthFixer(
        ip = BmbParameter(
          addressWidth = 16,
          dataWidth = 32,
          lengthWidth = 6,
          sourceWidth = 4,
          contextWidth = 3,
          alignmentMin = 4,
          canRead = true,
          canWrite = true,
          alignment = BmbParameter.BurstAlignement.WORD
        ),
        fixedWidth = 4
      )
      c
    }.doSimUntilVoid("test") { dut =>
      new BmbBridgeTester(
        master = dut.io.input,
        masterCd = dut.clockDomain,
        slave = dut.io.output,
        slaveCd = dut.clockDomain,
        alignmentMinWidth = dut.ip.access.alignmentMin
      )
    }
  }

}

class SpinalSimBmbLengthSpliterTester extends AnyFunSuite {
  for(w <- List(false, true); r <- List(false, true);   if w || r) {
    val header = "_" + (if (w) "w" else "") + (if (r) "r" else "")
    test("bypass" + header) {
      SimConfig.compile {
        val c = BmbAlignedSpliter(
          ip = BmbAccessParameter(
            addressWidth = 16,
            dataWidth = 32
          ).addSources(16, BmbSourceParameter(
            lengthWidth = 6,
            contextWidth = 3,
            alignmentMin = 0,
            canRead = r,
            canWrite = w,
            alignment = BmbParameter.BurstAlignement.WORD
          )).toBmbParameter(),
          lengthMax = 4
        )
        c
      }.doSimUntilVoid("test") { dut =>
        new BmbBridgeTester(
          master = dut.io.input,
          masterCd = dut.clockDomain,
          slave = dut.io.output,
          slaveCd = dut.clockDomain,
          alignmentMinWidth = dut.ip.access.alignmentMin
        )
      }
    }

    test("8" + header) {
      SimConfig.withWave.compile {
        val c = BmbAlignedSpliter(
          ip = BmbAccessParameter(
            addressWidth = 16,
            dataWidth = 32
          ).addSources(16, BmbSourceParameter(
            lengthWidth = 6,
            contextWidth = 8,
            alignmentMin = 0,
            canRead = r,
            canWrite = w,
            alignment = BmbParameter.BurstAlignement.WORD
          )).toBmbParameter(),
          lengthMax = 8
        )
        c
      }.doSimUntilVoid("test", 42) { dut =>
        new BmbBridgeTester(
          master = dut.io.input,
          masterCd = dut.clockDomain,
          slave = dut.io.output,
          slaveCd = dut.clockDomain,
          alignmentMinWidth = dut.ip.access.alignmentMin
        )
      }
    }

    test("16" + header) {
      SimConfig.compile {
        val c = BmbAlignedSpliter(
          ip = BmbAccessParameter(
            addressWidth = 16,
            dataWidth = 32
          ).addSources(16, BmbSourceParameter(
            lengthWidth = 6,
            contextWidth = 8,
            alignmentMin = 0,
            canRead = r,
            canWrite = w,
            alignment = BmbParameter.BurstAlignement.WORD
          )).toBmbParameter(),
          lengthMax = 16
        )
        c
      }.doSimUntilVoid("test") { dut =>
        new BmbBridgeTester(
          master = dut.io.input,
          masterCd = dut.clockDomain,
          slave = dut.io.output,
          slaveCd = dut.clockDomain,
          alignmentMinWidth = dut.ip.access.alignmentMin
        )
      }
    }
  }
}
